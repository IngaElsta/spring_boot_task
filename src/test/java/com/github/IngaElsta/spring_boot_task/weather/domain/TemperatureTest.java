package com.github.IngaElsta.spring_boot_task.weather.domain;

import com.github.IngaElsta.spring_boot_task.weather.domain.Temperature;
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
        String morning = "-3";
        String day = "2";
        String evening = "0";
        String night = "-5";

        Temperature temperature = new Temperature(morning, day, evening, night);
        Set<ConstraintViolation<Temperature>> violations = validator.validate(temperature);
        assertTrue(violations.isEmpty());
    }

    @Test
    void WhenNonDigitsNotPassed_thenValidationFails(){
        String morning = "a";
        String day = "2n";
        String evening = "*";
        String night = "-5";

        Temperature temperature = new Temperature(morning, day, evening, night);
        Set<ConstraintViolation<Temperature>> violations = validator.validate(temperature);
        assertFalse(violations.isEmpty());
    }

    @Test
    void WhenEmptyValuesPassed_thenValidationFails(){
        String evening = "5";

        Temperature temperature = new Temperature("", "", evening, "");
        Set<ConstraintViolation<Temperature>> violations = validator.validate(temperature);
        assertFalse(violations.isEmpty());
    }

    @Test
    void WhenNullValuesPassed_thenValidationFails(){
        String morning = "-5";
        String night = "-7";

        Temperature temperature = new Temperature(morning, null, null, night);
        Set<ConstraintViolation<Temperature>> violations = validator.validate(temperature);
        assertFalse(violations.isEmpty());
    }

    @Test
    void WhenInvalidNumericValuesPassed_thenValidationFails(){
        String morning = "-500"; //integer part too long
        String day = "2";
        String evening = "0";
        String night = "-5";

        Temperature temperature = new Temperature(morning, null, null, night);
        Set<ConstraintViolation<Temperature>> violations = validator.validate(temperature);
        assertFalse(violations.isEmpty());

        morning = "0.00001"; //fraction part too long

        temperature = new Temperature(morning, null, null, night);
        violations = validator.validate(temperature);
        assertFalse(violations.isEmpty());
    }
}
