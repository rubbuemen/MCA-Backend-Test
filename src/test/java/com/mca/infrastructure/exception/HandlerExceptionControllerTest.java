package com.mca.infrastructure.exception;

import com.mca.domain.model.dto.ErrorDto;
import feign.FeignException;
import feign.Request;
import feign.RequestTemplate;
import feign.RetryableException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpServerErrorException;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class HandlerExceptionControllerTest {

    @Autowired
    private HandlerExceptionController handlerExceptionController;

    private static final String GENERIC_DESCRIPTION = "There has been an error, please try later or contact with your administrator";

    @Test
    void testHandleGenericException() {
        var ex = new Exception();
        var expected = ErrorDto.builder().code("Generic exception").description(GENERIC_DESCRIPTION).build();
        var res = handlerExceptionController.handleException(ex);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, res.getStatusCode(), "Internal Server Error must be returned");
        assertEquals(expected, res.getBody(), "Expected code with generic info error");
    }

    @Test
    void testHandleHttpServerErrorException() {
        var ex = new HttpServerErrorException(HttpStatus.valueOf(504));
        var expected = ErrorDto.builder().code("Server exception").description(GENERIC_DESCRIPTION).build();
        var res = handlerExceptionController.handleException(ex);
        assertEquals(HttpStatus.valueOf(504), res.getStatusCode(), "504 Error must be returned");
        assertEquals(expected, res.getBody(), "Expected code with http info error");
    }

    @Test
    void testHandleFeignNotFoundException() {
        var ex = new FeignException.NotFound("Not found", Request.create(Request.HttpMethod.GET, "test", new HashMap<>(), null, new RequestTemplate()), null, null);
        var expected = ErrorDto.builder().code("Client exception").description("There seems to be a problem connecting to the client providing the information, try again later or contact an administrator.").build();
        var res = handlerExceptionController.handleException(ex);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, res.getStatusCode(), "Internal Server Error must be returned");
        assertEquals(expected, res.getBody(), "Expected code with feign client info error");
    }

    @Test
    void testHandleFeignRefusedConnectionException() {
        var ex = new RetryableException(0, "", Request.HttpMethod.GET, 0L, Request.create(Request.HttpMethod.GET, "test", new HashMap<>(), null, new RequestTemplate()));
        var expected = ErrorDto.builder().code("Client exception").description("There seems to be a problem connecting to the client providing the information, try again later or contact an administrator.").build();
        var res = handlerExceptionController.handleException(ex);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, res.getStatusCode(), "Internal Server Error must be returned");
        assertEquals(expected, res.getBody(), "Expected code with feign client info error");
    }

    @Test
    void testHandleIllegalArgumentException() {
        var ex = new IllegalArgumentException("test");
        var expected = ErrorDto.builder().code("Illegal argument exception").description("test").build();
        var res = handlerExceptionController.handleException(ex);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, res.getStatusCode(), "Internal Server Error must be returned");
        assertEquals(expected, res.getBody(), "Expected code with illegal argument info error");
    }

    @Test
    void testHandleServiceException() {
        var ex = new ServiceException("test");
        var expected = ErrorDto.builder().code("Service exception").description("test").build();
        var res = handlerExceptionController.handleException(ex);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, res.getStatusCode(), "Internal Server Error must be returned");
        assertEquals(expected, res.getBody(), "Expected code with service info error");
    }


}