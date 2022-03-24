package com.github.IngaElsta.spring_boot_task.weather.domain;

import com.github.IngaElsta.spring_boot_task.weather.domain.Wind;
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
        Double speed = 5.4;
        Double gusts = 7.2;
        String direction = "NW";

        Wind location = new Wind(speed, gusts, direction);
        Set<ConstraintViolation<Wind>> violations = validator.validate(location);
        assertTrue(violations.isEmpty());
    }

    @Test
    void WhenWindDirectionIsEmpty_thenValidationSucceeds(){
        Double speed = 5.0;
        Double gusts = 7.2;

        Wind location = new Wind(speed, gusts, null);
        Set<ConstraintViolation<Wind>> violations = validator.validate(location);
        assertTrue(violations.isEmpty());
    }

    @Test
    void WhenGustSpeedNotPassed_thenValidationFails(){
        Double speed = 5.8;

        Wind location = new Wind(speed, null, "NW");
        Set<ConstraintViolation<Wind>> violations = validator.validate(location);
        assertFalse(violations.isEmpty());
    }

    @Test
    void WhenWindSpeedNotPassed_thenValidationFails(){
        Double gusts = 7.2;
        String direction = "NW";

        Wind location = new Wind(null, gusts, direction);
        Set<ConstraintViolation<Wind>> violations = validator.validate(location);
        assertFalse(violations.isEmpty());
    }

    @Test
    void WhenWindDirectionNotPassed_thenValidationFails(){
        Double speed = 5.0;
        Double gusts = 7.2;

        Wind location = new Wind(null, gusts, "");
        Set<ConstraintViolation<Wind>> violations = validator.validate(location);
        assertFalse(violations.isEmpty());
    }

    @Test
    void WhenInvalidWindSpeedPassed_thenValidationFails() {
        Double speed = 1010.4;
        Double gusts = 7.2;
        String direction = "NW";

        Wind location = new Wind(speed, gusts, direction);
        Set<ConstraintViolation<Wind>> violations = validator.validate(location);
        assertFalse(violations.isEmpty());

        speed = -1.9;
        location = new Wind(speed, gusts, direction);
        violations = validator.validate(location);
        assertFalse(violations.isEmpty());

        speed = 0.123;
        location = new Wind(speed, gusts, direction);
        violations = validator.validate(location);
        assertFalse(violations.isEmpty());
    }

    @Test
    void WhenInvalidGustSpeedPassed_thenValidationFails() {
        Double speed = 5.6;
        Double gusts = 1100.0;
        String direction = "NW";
        Wind location = new Wind(speed, gusts, direction);
        Set<ConstraintViolation<Wind>> violations = validator.validate(location);
        assertFalse(violations.isEmpty());

        gusts = -3.5;
        location = new Wind(speed, gusts, direction);
        violations = validator.validate(location);
        assertFalse(violations.isEmpty());

        gusts = 0.123;
        location = new Wind(speed, gusts, direction);
        violations = validator.validate(location);
        assertFalse(violations.isEmpty());
    }

}
