package com.rentalcars.contract.model.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(ElementType.TYPE)
@Retention(RUNTIME)
@Constraint(validatedBy = DateValidation.class)
public @interface DateValidator {

    String message() default "Date of return must be after date of rent";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}