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
    void ItIsPossibleToRetrieveAndResetAnyValue(){
        Temperature temperature = new Temperature(1.5, 10.4, 7.1, -2.05);
        Double morning = temperature.getMorn();
        Double day = temperature.getDay();
        Double evening = temperature.getEve();
        Double night =  temperature.getNight();

        temperature.setMorn(0.0);
        temperature.setDay(4.5);
        temperature.setEve(2.1);
        temperature.setNight(-1.2);
    }
}
