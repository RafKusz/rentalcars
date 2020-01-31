package com.rentalcars.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(value = {CarNotFoundException.class, UserNotFoundException.class, ContractNotFoundException.class})
    public ResponseEntity<ResponseStatus> catchNotFoundException(Exception e) {
        ResponseStatus responseStatus = new ResponseStatus(NOT_FOUND.value(), e.getMessage());
        return new ResponseEntity<ResponseStatus>(responseStatus, NOT_FOUND);
    }

    @ExceptionHandler(value = {ContractUnavailableException.class})
    public ResponseEntity<ResponseStatus> catchConflictException(Exception e) {
        ResponseStatus responseStatus = new ResponseStatus(CONFLICT.value(), e.getMessage());
        return new ResponseEntity<ResponseStatus>(responseStatus, CONFLICT);
    }
}