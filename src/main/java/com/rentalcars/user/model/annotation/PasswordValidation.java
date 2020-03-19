package com.rentalcars.user.model.annotation;

import com.rentalcars.user.model.UserInput;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordValidation implements ConstraintValidator<PasswordMatcher, UserInput> {
    @Override
    public boolean isValid(UserInput userInput, ConstraintValidatorContext context) {
        return userInput.getPassword().equals(userInput.getPasswordConfirm());
    }
}