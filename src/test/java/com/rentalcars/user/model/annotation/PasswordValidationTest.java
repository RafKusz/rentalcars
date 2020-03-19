package com.rentalcars.user.model.annotation;

import com.rentalcars.user.UserFixtures;
import com.rentalcars.user.model.UserInput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class PasswordValidationTest {

    private static final String INCORRECT_PASSWORD = "incorrect password";

    @Autowired
    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Validation not pass if password is not equals passwordConfirm")
    public void validationFailIfPasswordIsNotEqualsPasswordConfirm() {
        UserInput userInput = UserFixtures.getUserInput();
        userInput.setPasswordConfirm(INCORRECT_PASSWORD);

        Set<ConstraintViolation<UserInput>> violations = validator.validate(userInput);

        assertFalse(violations.isEmpty());
        assertEquals("The password fields must match", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("Validation pass if password is equals passwordConfirm")
    public void validationPassIfPasswordIsEqualsPasswordConfirm() {
        UserInput userInput = UserFixtures.getUserInput();

        Set<ConstraintViolation<UserInput>> violations = validator.validate(userInput);

        assertTrue(violations.isEmpty());
    }
}