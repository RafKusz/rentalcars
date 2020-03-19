package com.rentalcars.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReplyStatus {

    private int status;
    private String message;
}