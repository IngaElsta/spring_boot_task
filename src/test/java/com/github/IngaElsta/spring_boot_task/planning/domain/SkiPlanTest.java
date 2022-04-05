package com.github.IngaElsta.spring_boot_task.planning.domain;

import com.github.IngaElsta.spring_boot_task.weather.domain.Alert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SkiPlanTest {

    private Validator validator;
    private SkiLocation location = new SkiLocation(56.95, 24.11);
    private LocalDate date = LocalDate.now();

    @BeforeEach
    public void setup() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void WhenAllDataValid_thenValidationSucceeds(){
        SkiPlan skiPlan = new SkiPlan(location, date);

        Set<ConstraintViolation<SkiPlan>> violations = validator.validate(skiPlan);
        assertTrue(violations.isEmpty());
    }

    @Test
    void WhenAnyVariableIsNull_thenValidationFails(){
        SkiPlan skiPlan = new SkiPlan(location, null);
        Set<ConstraintViolation<SkiPlan>> violations = validator.validate(skiPlan);
        assertFalse(violations.isEmpty());

        skiPlan = new SkiPlan(null, date);
        violations = validator.validate(skiPlan);
        assertFalse(violations.isEmpty());
    }

    @Test
    void ItIsPossibleToRetrieveAndResetAnyValue() {
        SkiPlan skiPlan = new SkiPlan(location, date);

        assertEquals(skiPlan.getLocation(), location);
        assertEquals(skiPlan.getSkiingDate(), date);

        skiPlan.setLocation(new SkiLocation(89.6, -2.1));
        skiPlan.setSkiingDate(LocalDate.now().plusDays(4));
    }

}