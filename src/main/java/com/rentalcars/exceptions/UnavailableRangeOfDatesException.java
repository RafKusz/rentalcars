package com.rentalcars.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.CONFLICT;

@ResponseStatus(CONFLICT)
@Slf4j
public class UnavailableRangeOfDatesException extends Exception {
    public UnavailableRangeOfDatesException() {
        super();
    }

    public UnavailableRangeOfDatesException(String message) {
        super(message);
        log.info(message);
    }
}
