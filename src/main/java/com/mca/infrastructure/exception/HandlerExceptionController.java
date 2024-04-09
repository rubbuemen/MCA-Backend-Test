package com.mca.infrastructure.exception;

import com.mca.domain.model.dto.ErrorDto;
import feign.FeignException;
import feign.RetryableException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException;

@RestControllerAdvice
@Slf4j
public class HandlerExceptionController {

    private static final String LOG_PATTERN = " exception handler {}: {}";
    private static final String GENERIC_DESCRIPTION = "There has been an error, please try later or contact with your administrator";

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleException(Exception ex) {
        return switch (ex) {
            case HttpServerErrorException e -> handleHttpServerErrorException(e);
            case FeignException.NotFound e -> handleFeignNotFoundException(e);
            case RetryableException e -> handleFeignRefusedConnectionException(e);
            case IllegalArgumentException e -> handleIllegalArgumentException(e);
            case ServiceException e -> handleServiceException(e);
            default -> handleGenericException(ex);
        };
    }

    private ResponseEntity<ErrorDto> handleGenericException(Exception ex) {
        log.warn("Generic error".concat(LOG_PATTERN), ex.getClass(), ex.getMessage(), ex);
        var err = ErrorDto.builder()
                .code("Generic exception")
                .description(GENERIC_DESCRIPTION).build();
        return ResponseEntity.internalServerError().body(err);
    }

    private ResponseEntity<ErrorDto> handleHttpServerErrorException(HttpServerErrorException ex) {
        log.warn("HTTP Server Error".concat(LOG_PATTERN), ex.getClass(), ex.getMessage(), ex);
        var err = ErrorDto.builder()
                .code("Server exception")
                .description(GENERIC_DESCRIPTION).build();
        return ResponseEntity.status(ex.getStatusCode()).body(err);
    }

    private ResponseEntity<ErrorDto> handleFeignNotFoundException(FeignException.NotFound ex) {
        log.warn("Feign client not found error".concat(LOG_PATTERN), ex.getClass(), ex.getMessage(), ex);
        var err = ErrorDto.builder()
                .code("Client exception")
                .description("There seems to be a problem connecting to the client providing the information, try again later or contact an administrator.").build();
        return ResponseEntity.internalServerError().body(err);
    }

    private ResponseEntity<ErrorDto> handleFeignRefusedConnectionException(RetryableException ex) {
        log.warn("Feign client refused connection".concat(LOG_PATTERN), ex.getClass(), ex.getMessage(), ex);
        var err = ErrorDto.builder()
                .code("Client exception")
                .description("There seems to be a problem connecting to the client providing the information, try again later or contact an administrator.").build();
        return ResponseEntity.internalServerError().body(err);
    }

    private ResponseEntity<ErrorDto> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.warn("Illegal argument error".concat(LOG_PATTERN), ex.getClass(), ex.getMessage(), ex);
        var err = ErrorDto.builder()
                .code("Illegal argument exception")
                .description(ex.getMessage()).build();
        return ResponseEntity.internalServerError().body(err);
    }

    private ResponseEntity<ErrorDto> handleServiceException(ServiceException ex) {
        log.warn("Service error".concat(LOG_PATTERN), ex.getClass(), ex.getMessage(), ex);
        var err = ErrorDto.builder()
                .code("Service exception")
                .description(ex.getMessage()).build();
        return ResponseEntity.internalServerError().body(err);
    }
}
