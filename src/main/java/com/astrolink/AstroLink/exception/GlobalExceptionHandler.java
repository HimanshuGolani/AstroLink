package com.astrolink.AstroLink.exception;

import com.astrolink.AstroLink.exception.custom.DataNotFoundException;
import com.astrolink.AstroLink.exception.custom.EmailAlreadyExistsException;
import com.astrolink.AstroLink.exception.custom.PaymentException;
import com.mongodb.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleDataNotFoundException(DataNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse("DATA_NOT_FOUND", ex.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PaymentException.class)
    public ResponseEntity<ErrorResponse> handlePaymentException(PaymentException ex) {
        ErrorResponse errorResponse = new ErrorResponse("PAYMENT_ERROR", ex.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleEmailAlreadyExists(EmailAlreadyExistsException ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.CONFLICT.toString(),
                ex.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }


    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateKeyException(DuplicateKeyException ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.CONFLICT.toString(),
                "A record with the same unique key already exists: " + ex.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                "METHOD_NOT_ALLOWED",
                "HTTP method not supported for this endpoint. Supported methods: " + ex.getSupportedHttpMethods(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
        System.out.println(ex);
        ErrorResponse errorResponse = new ErrorResponse("INTERNAL_SERVER_ERROR", "An unexpected error occurred", LocalDateTime.now());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
