package com.rentalcars.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
@Slf4j
public class CarNotFoundException extends Exception {

    public CarNotFoundException() {
        super();
    }

    public CarNotFoundException(String message) {
        super(message);
        log.info(message);
    }

}
