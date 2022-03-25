package com.github.IngaElsta.spring_boot_task.weather.domain;

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
    void WhenAllValuesPassed_thenValidationSucceeds(){
        Double speed = 5.4;
        Double gusts = 7.2;
        String direction = "NW";

        Wind wind = new Wind(speed, gusts, direction);
        Set<ConstraintViolation<Wind>> violations = validator.validate(wind);
        assertTrue(violations.isEmpty());
    }

    @Test
    void WhenEitherValueIsEmpty_thenValidationSucceeds(){
        Double speed = 5.4;
        Double gusts = 7.2;
        String direction = "NW";

        Wind wind = new Wind(speed, gusts, null);
        Set<ConstraintViolation<Wind>> violations = validator.validate(wind);
        assertTrue(violations.isEmpty());


        wind = new Wind(speed, null, direction);
        violations = validator.validate(wind);
        assertTrue(violations.isEmpty());

        wind = new Wind(null, gusts, direction);
        violations = validator.validate(wind);
        assertTrue(violations.isEmpty());
        assertTrue(violations.isEmpty());
    }

    @Test
    void WhenInvalidWindSpeedPassed_thenValidationFails() {
        Double speed = 1010.4;
        Double gusts = 7.2;
        String direction = "NW";

        Wind wind = new Wind(speed, gusts, direction);
        Set<ConstraintViolation<Wind>> violations = validator.validate(wind);
        assertFalse(violations.isEmpty());

        speed = -1.9;
        wind = new Wind(speed, gusts, direction);
        violations = validator.validate(wind);
        assertFalse(violations.isEmpty());

        speed = 0.123;
        wind = new Wind(speed, gusts, direction);
        violations = validator.validate(wind);
        assertFalse(violations.isEmpty());
    }

    @Test
    void WhenInvalidGustSpeedPassed_thenValidationFails() {
        Double speed = 5.6;
        Double gusts = 1100.0;
        String direction = "NW";
        Wind wind = new Wind(speed, gusts, direction);
        Set<ConstraintViolation<Wind>> violations = validator.validate(wind);
        assertFalse(violations.isEmpty());

        gusts = -3.5;
        wind = new Wind(speed, gusts, direction);
        violations = validator.validate(wind);
        assertFalse(violations.isEmpty());

        gusts = 0.123;
        wind = new Wind(speed, gusts, direction);
        violations = validator.validate(wind);
        assertFalse(violations.isEmpty());
    }

    @Test
    void ItIsPossibleToRetrieveAnyValue() {
        Wind wind = new Wind(2.5, 6.0, "S");
        wind.getSpeed();
        wind.getGusts();
        wind.getDirection();
    }

    @Test
    void ItIsPossibleToUpdateAnyValue() {
        Wind wind = new Wind(2.5, 6.0, "S");
        wind.setSpeed(16.9);
        wind.setGusts(23.1);
        wind.setDirection("W");
    }

}
