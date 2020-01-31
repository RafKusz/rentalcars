package com.rentalcars.contract.model.annotation;

import com.rentalcars.contract.model.ContractInput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static com.rentalcars.contract.ContractFixtures.getRentContractInput;
import static org.junit.jupiter.api.Assertions.*;

public class DateValidationTest {

    @Autowired
    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Validation not pass if date of return is before than date of rent")
    public void validationFailIfDateOfReturnIsBeforeThanDateOfRent() {
        ContractInput contractInput = getRentContractInput();
        contractInput.setDateOfRent(contractInput.getDateOfReturn().plusDays(1));

        Set<ConstraintViolation<ContractInput>> violations = validator.validate(contractInput);

        assertFalse(violations.isEmpty());
        assertEquals("Date of return must be after date of rent", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("Validation pass if date of return is after than date of rent")
    public void validationPassIfDateOfReturnIsAfterThanDateOfRent() {
        ContractInput contractInput = getRentContractInput();

        Set<ConstraintViolation<ContractInput>> violations = validator.validate(contractInput);

        assertTrue(violations.isEmpty());
    }
}