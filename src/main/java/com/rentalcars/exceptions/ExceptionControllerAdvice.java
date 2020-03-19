package com.rentalcars.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(value = {CarNotFoundException.class, UserNotFoundException.class, ContractNotFoundException.class})
    public ResponseEntity<ReplyStatus> catchNotFoundException(Exception e) {
        ReplyStatus replyStatus = new ReplyStatus(NOT_FOUND.value(), e.getMessage());
        return new ResponseEntity<ReplyStatus>(replyStatus, NOT_FOUND);
    }

    @ExceptionHandler(value = {ContractUnavailableException.class, EmailIsAlreadyExistException.class, UnavailableRangeOfDatesException.class})
    public ResponseEntity<ReplyStatus> catchConflictException(Exception e) {
        ReplyStatus replyStatus = new ReplyStatus(CONFLICT.value(), e.getMessage());
        return new ResponseEntity<ReplyStatus>(replyStatus, CONFLICT);
    }
}