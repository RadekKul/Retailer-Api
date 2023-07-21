package com.company.retailer.common.exception.handler;

import com.company.retailer.common.exception.AbstractBadRequestException;
import com.company.retailer.common.exception.AbstractNotFoundException;
import com.company.retailer.common.exception.RequestValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AbstractNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ResponseEntity<String> handleNotFoundException(RuntimeException exception) {
        log.error("ExceptionHandler: an Not Found error occurred with message: {}", exception.getMessage());
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AbstractBadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<String> handleBadRequestException(RuntimeException exception) {
        log.error("ExceptionHandler: an Bad Request error occurred with message: {}", exception.getMessage());
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RequestValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<RequestValidationResponse> handleRequestValidationException(RequestValidationException exception) {
        RequestValidationResponse error = new RequestValidationResponse(exception.getErrors());
        log.error("ExceptionHandler: an request validation error occurred. Error details: {}", exception.getErrors());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
