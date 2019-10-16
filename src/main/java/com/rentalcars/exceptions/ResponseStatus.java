package com.rentalcars.exceptions;

import lombok.Data;

@Data
public class ResponseStatus {

    private int status;
    private String message;

    public ResponseStatus(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}