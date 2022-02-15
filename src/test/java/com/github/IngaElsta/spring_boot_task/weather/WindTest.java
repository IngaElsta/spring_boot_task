package com.github.IngaElsta.spring_boot_task.weather;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WindTest {

    private Validator validator;

    @BeforeEach
    public void setup() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void WhenWindSpeedValuesAreAcceptable_thenValidationSucceeds(){
        String speed = "5";
        String gusts = "7.2";
        String direction = "NW";

        Wind location = new Wind(speed, gusts, direction);
        Set<ConstraintViolation<Wind>> violations = validator.validate(location);
        assertTrue(violations.isEmpty());
    }

    @Test
    void WhenWindDirectionIsEmpty_thenValidationSucceeds(){
        String speed = "5";
        String gusts = "7.2";

        Wind location = new Wind(speed, gusts, null);
        Set<ConstraintViolation<Wind>> violations = validator.validate(location);
        assertTrue(violations.isEmpty());
    }

    @Test
    void WhenGustSpeedNotPassed_thenValidationFails(){
        String speed = "5";

        Wind location = new Wind(speed, "", "NW");
        Set<ConstraintViolation<Wind>> violations = validator.validate(location);
        assertFalse(violations.isEmpty());
    }

    @Test
    void WhenWindSpeedNotPassed_thenValidationFails(){
        String gusts = "7.2";
        String direction = "NW";

        Wind location = new Wind(null, gusts, direction);
        Set<ConstraintViolation<Wind>> violations = validator.validate(location);
        assertFalse(violations.isEmpty());
    }

    @Test
    void WhenWindDirectionNotPassed_thenValidationFails(){
        String speed = "5";
        String gusts = "7.2";

        Wind location = new Wind(null, gusts, "");
        Set<ConstraintViolation<Wind>> violations = validator.validate(location);
        assertFalse(violations.isEmpty());
    }

    @Test
    void WhenNonDigitsNotPassed_thenValidationFails(){
        String speed = "a";
        String gusts = "3.b";
        String direction = "NW";

        Wind location = new Wind(speed, gusts, direction);
        Set<ConstraintViolation<Wind>> violations = validator.validate(location);
        assertFalse(violations.isEmpty());
    }

    @Test
    void WhenInvalidWindSpeedPassed_thenValidationFails() {
        String speed = "1010";
        String gusts = "7.2";
        String direction = "NW";

        Wind location = new Wind(speed, gusts, direction);
        Set<ConstraintViolation<Wind>> violations = validator.validate(location);
        assertFalse(violations.isEmpty());

        speed = "-1";
        location = new Wind(speed, gusts, direction);
        violations = validator.validate(location);
        assertFalse(violations.isEmpty());

        speed = "0.123";
        location = new Wind(speed, gusts, direction);
        violations = validator.validate(location);
        assertFalse(violations.isEmpty());
    }

    @Test
    void WhenInvalidGustSpeedPassed_thenValidationFails() {
        String speed = "5";
        String gusts = "1100";String direction = "NW";
        Wind location = new Wind(speed, gusts, direction);
        Set<ConstraintViolation<Wind>> violations = validator.validate(location);
        assertFalse(violations.isEmpty());

        gusts = "-3.5";
        location = new Wind(speed, gusts, direction);
        violations = validator.validate(location);
        assertFalse(violations.isEmpty());

        gusts = "0.123";
        location = new Wind(speed, gusts, direction);
        violations = validator.validate(location);
        assertFalse(violations.isEmpty());
    }

}
