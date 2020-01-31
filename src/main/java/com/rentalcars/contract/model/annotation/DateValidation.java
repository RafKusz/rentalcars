package com.rentalcars.contract.model.annotation;

import com.rentalcars.contract.model.ContractInput;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DateValidation implements ConstraintValidator<DateValidator, ContractInput> {
    @Override
    public boolean isValid(ContractInput contractInput, ConstraintValidatorContext context) {
        return contractInput.getDateOfReturn().isAfter(contractInput.getDateOfRent());
    }
}