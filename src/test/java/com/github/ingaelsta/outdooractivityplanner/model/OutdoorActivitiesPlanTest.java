package com.github.ingaelsta.outdooractivityplanner.model;

import com.github.ingaelsta.outdooractivityplanner.planning.entity.OutdoorActivitiesLocation;
import com.github.ingaelsta.outdooractivityplanner.planning.model.OutdoorActivitiesPlan;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class OutdoorActivitiesPlanTest {

    private Validator validator;
    private OutdoorActivitiesLocation location = new OutdoorActivitiesLocation(56.95, 24.11);
    private LocalDate date = LocalDate.now();

    @BeforeEach
    public void setup() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void WhenAllDataValid_thenValidationSucceeds(){
        OutdoorActivitiesPlan outdoorActivitiesPlan = new OutdoorActivitiesPlan(location, date);

        Set<ConstraintViolation<OutdoorActivitiesPlan>> violations = validator.validate(outdoorActivitiesPlan);
        assertTrue(violations.isEmpty());
    }

    @Test
    void WhenAnyVariableIsNull_thenValidationFails(){
        OutdoorActivitiesPlan outdoorActivitiesPlan = new OutdoorActivitiesPlan(location, null);
        Set<ConstraintViolation<OutdoorActivitiesPlan>> violations = validator.validate(outdoorActivitiesPlan);
        assertFalse(violations.isEmpty());

        outdoorActivitiesPlan = new OutdoorActivitiesPlan(null, date);
        violations = validator.validate(outdoorActivitiesPlan);
        assertFalse(violations.isEmpty());
    }

    @Test
    void ItIsPossibleToRetrieveAndResetAnyValue() {
        OutdoorActivitiesPlan outdoorActivitiesPlan = new OutdoorActivitiesPlan(location, date);

        assertEquals(outdoorActivitiesPlan.getLocation(), location);
        assertEquals(outdoorActivitiesPlan.getDate(), date);

        outdoorActivitiesPlan.setLocation(new OutdoorActivitiesLocation(89.6, -2.1));
        outdoorActivitiesPlan.setDate(LocalDate.now().plusDays(4));
    }

}