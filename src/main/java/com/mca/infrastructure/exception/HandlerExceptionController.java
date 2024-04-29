package com.mca.infrastructure.exception;

import com.mca.domain.model.dto.ApiError;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
@Slf4j
public class HandlerExceptionController {

    private static final String GENERIC_DESCRIPTION = "There has been an error, please try later or contact with your administrator to check logs";

    @ExceptionHandler(NoResourceFoundException.class)
    private ResponseEntity<ApiError> handleNoResourceFoundException(NoResourceFoundException ex) {
        log.warn("NoResourceFoundException thrown {}: {}", ex.getClass(), ex.getMessage(), ex);
        var err = ApiError.builder()
                .message("Resource not found")
                .description("The specified resource does not exist")
                .code(ex.getStatusCode().value()).build();
        return ResponseEntity.status(ex.getStatusCode()).body(err);
    }

    @ExceptionHandler(GameRequestValidationException.class)
    private ResponseEntity<ApiError> handleGameRequestValidationException(GameRequestValidationException ex) {
        log.warn("GameRequestValidationException thrown {}: {}", ex.getClass(), ex.getMessage(), ex);
        var err = ApiError.builder()
                .message("Error in the validation of the game request")
                .description(ex.getMessage())
                .code(HttpStatus.BAD_REQUEST.value()).build();
        return ResponseEntity.badRequest().body(err);
    }

    @ExceptionHandler(FeignException.class)
    private ResponseEntity<ApiError> handleFeignException(FeignException ex) {
        log.warn("FeignException thrown {}: {}", ex.getClass(), ex.getMessage(), ex);
        ApiError err;
        if (ex.status() == 404) {
            err = ApiError.builder()
                    .message("Feigned resource not found")
                    .description("The specified resource could not be obtained from feign client")
                    .code(ex.status()).build();
        } else if (ex.status() == 400) {
            err = ApiError.builder()
                    .message("Feigned resource not validated")
                    .description("The specified resource could not be validated")
                    .code(ex.status()).build();
        } else if (ex.status() == -1) {
            err = ApiError.builder()
                    .message("Problem with feign client")
                    .description("The feign client may not be available at this time.")
                    .code(HttpStatus.SERVICE_UNAVAILABLE.value()).build();
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE.value()).body(err);
        } else {
            err = ApiError.builder()
                    .message("Problem with feign client")
                    .description(GENERIC_DESCRIPTION)
                    .code(ex.status()).build();
        }
        return ResponseEntity.status(ex.status()).body(err);
    }

    @ExceptionHandler(Exception.class)
    private ResponseEntity<ApiError> handleGenericException(Exception ex) {
        log.warn("Generic exception thrown {}: {}", ex.getClass(), ex.getMessage(), ex);
        var err = ApiError.builder()
                .message("Ups! Something occurred")
                .description(GENERIC_DESCRIPTION)
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value()).build();
        return ResponseEntity.internalServerError().body(err);
    }
}

