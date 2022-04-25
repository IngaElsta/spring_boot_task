package com.github.ingaelsta.outdooractivityplanner.planning.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class OutdoorActivityTest {
    private Validator validator;
    private LocalDate planDate = LocalDate.now();
    private final Double latitude = 52.1;
    private final Double longitude = -0.78;

    @BeforeEach
    public void setup() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void WhenAllDataValid_thenValidationSucceeds(){
        OutdoorActivity outdoorActivity = new OutdoorActivity(latitude, longitude, planDate);

        Set<ConstraintViolation<OutdoorActivity>> violations = validator.validate(outdoorActivity);
        assertTrue(violations.isEmpty());
    }

    @Test
    void WhenAnyVariableIsNull_thenValidationFails(){
        OutdoorActivity outdoorActivity = new OutdoorActivity(latitude, longitude, null);
        Set<ConstraintViolation<OutdoorActivity>> violations = validator.validate(outdoorActivity);
        assertFalse(violations.isEmpty());

        outdoorActivity = new OutdoorActivity(null, longitude, planDate);
        violations = validator.validate(outdoorActivity);
        assertFalse(violations.isEmpty());

        outdoorActivity = new OutdoorActivity(latitude, null, planDate);
        violations = validator.validate(outdoorActivity);
        assertFalse(violations.isEmpty());
    }

    @Test
    void WhenInvalidCoordinatesPassed_thenValidationFails() {
        Double longitude = 1900.9; //larger than max
        OutdoorActivity outdoorActivity = new OutdoorActivity(latitude, longitude, planDate);
        Set<ConstraintViolation<OutdoorActivity>> violations = validator.validate(outdoorActivity);
        assertFalse(violations.isEmpty());
        
        Double latitude = 100.0; //larger than max
        outdoorActivity = new OutdoorActivity(latitude, longitude, planDate);
        violations = validator.validate(outdoorActivity);
        assertFalse(violations.isEmpty());
    }

    @Test
    void ItIsPossibleToRetrieveAndResetValues() {
        OutdoorActivity outdoorActivity = new OutdoorActivity(latitude, longitude, planDate);

        assertEquals(outdoorActivity.getLatitude(), latitude);
        assertEquals(outdoorActivity.getLongitude(),longitude);
        assertEquals(outdoorActivity.getPlanDate(), planDate);

        outdoorActivity.setLatitude(89.6);
        outdoorActivity.setLongitude(-2.1);
        outdoorActivity.setPlanDate(LocalDate.now().plusDays(4));
    }
    
}
