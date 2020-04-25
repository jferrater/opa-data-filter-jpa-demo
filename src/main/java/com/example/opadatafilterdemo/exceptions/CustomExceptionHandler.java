package com.example.opadatafilterdemo.exceptions;

import opa.datafilter.core.ast.db.query.exception.PartialEvauationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author joffryferrater
 */
@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(PartialEvauationException.class)
    public ResponseEntity<ApiError> handlePetProfileNotFoundException(PartialEvauationException e) {
        ApiError apiError = new ApiError(404, "Not Found", e.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleException(Exception e) {
        e.printStackTrace();
        ApiError apiError = new ApiError(500, "Internal Server Error", e.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
