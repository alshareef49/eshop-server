package com.eshop.utility;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.List;


import com.eshop.exception.EShopException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;

class ExceptionControllerAdviceTest {

    @InjectMocks
    private ExceptionControllerAdvice exceptionControllerAdvice;

    @Mock
    private Environment environment;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnInternalServerErrorForGeneralException() {

        Exception exception = new Exception("Some unexpected error");
        when(environment.getProperty("General.EXCEPTION_MESSAGE")).thenReturn("An error occurred: ");

        ResponseEntity<ErrorInfo> response = exceptionControllerAdvice.generalExceptionHandler(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An error occurred: Some unexpected error", response.getBody().getErrorMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getBody().getErrorCode());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    void shouldReturnBadRequestForEShopException() {
        EShopException exception = new EShopException("CustomerCartService.NO_CART_FOUND");
        when(environment.getProperty("CustomerCartService.NO_CART_FOUND")).thenReturn("No cart found for the user.");

        ResponseEntity<ErrorInfo> response = exceptionControllerAdvice.eshopExceptionHandler(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("No cart found for the user.", response.getBody().getErrorMessage());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().getErrorCode());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    void shouldReturnBadRequestForMethodArgumentNotValidException() {

        BindingResult bindingResult = mock(BindingResult.class);
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(null, bindingResult);

        when(bindingResult.getAllErrors()).thenReturn(
                List.of(new ObjectError("field", "Field cannot be empty"),
                        new ObjectError("email", "Invalid email format")));

        ResponseEntity<ErrorInfo> response = exceptionControllerAdvice.exceptionHandler(exception);


        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Field cannot be empty, Invalid email format", response.getBody().getErrorMessage());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().getErrorCode());
        assertNotNull(response.getBody().getTimestamp());
    }

}
