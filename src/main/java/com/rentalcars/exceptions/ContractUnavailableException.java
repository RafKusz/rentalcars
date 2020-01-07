package com.rentalcars.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
@Slf4j
public class ContractUnavailableException extends Exception {
    public ContractUnavailableException() {
        super();
    }

    public ContractUnavailableException(String message) {
        super(message);
        log.info(message);
    }
}
