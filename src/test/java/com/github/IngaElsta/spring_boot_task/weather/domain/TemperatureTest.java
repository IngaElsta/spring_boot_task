package com.github.IngaElsta.spring_boot_task.weather.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

public class TemperatureTest {

    private Validator validator;

    @BeforeEach
    public void setup() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void WhenAcceptableValuesPassed_thenValidationSucceeds(){
        Double  morning = -3.0;
        Double  day = 2.6;
        Double  evening = 0.5;
        Double  night = -5.7;

        Temperature temperature = new Temperature(morning, day, evening, night);
        Set<ConstraintViolation<Temperature>> violations = validator.validate(temperature);
        assertTrue(violations.isEmpty());
    }

    @Test
    void WhenNullValuesPassed_thenValidationFails(){
        Double  morning = -5.1;
        Double  night = -7.4;

        Temperature temperature = new Temperature(morning, null, null, night);
        Set<ConstraintViolation<Temperature>> violations = validator.validate(temperature);
        assertFalse(violations.isEmpty());
    }

    @Test
    void WhenInvalidNumericValuesPassed_thenValidationFails(){
        Double  morning = -500.2; //Integer part too long
        Double  night = -5.8;

        Temperature temperature = new Temperature(morning, null, null, night);
        Set<ConstraintViolation<Temperature>> violations = validator.validate(temperature);
        assertFalse(violations.isEmpty());

        morning = 0.00001; //fraction part too long

        temperature = new Temperature(morning, null, null, night);
        violations = validator.validate(temperature);
        assertFalse(violations.isEmpty());
    }
}
