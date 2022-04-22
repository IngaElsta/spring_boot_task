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

public class OutdoorActivitiesPlanDAOTest {
    private Validator validator;
    private LocalDate date = LocalDate.now();
    private final Double latitude = 52.1;
    private final Double longitude = -0.78;

    @BeforeEach
    public void setup() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void WhenAllDataValid_thenValidationSucceeds(){
        OutdoorActivitiesDAO planDAO = new OutdoorActivitiesDAO(latitude, longitude, date);

        Set<ConstraintViolation<OutdoorActivitiesDAO>> violations = validator.validate(planDAO);
        assertTrue(violations.isEmpty());
    }

    @Test
    void WhenAnyVariableIsNull_thenValidationFails(){
        OutdoorActivitiesDAO planDAO = new OutdoorActivitiesDAO(latitude, longitude, null);
        Set<ConstraintViolation<OutdoorActivitiesDAO>> violations = validator.validate(planDAO);
        assertFalse(violations.isEmpty());

        planDAO = new OutdoorActivitiesDAO(null, longitude, date);
        violations = validator.validate(planDAO);
        assertFalse(violations.isEmpty());

        planDAO = new OutdoorActivitiesDAO(latitude, null, date);
        violations = validator.validate(planDAO);
        assertFalse(violations.isEmpty());
    }

    @Test
    void WhenInvalidCoordinatesPassed_thenValidationFails() {
        Double longitude = 1900.9; //larger than max
        OutdoorActivitiesDAO planDAO = new OutdoorActivitiesDAO(latitude, longitude, date);
        Set<ConstraintViolation<OutdoorActivitiesDAO>> violations = validator.validate(planDAO);
        assertFalse(violations.isEmpty());
        
        Double latitude = 100.0; //larger than max
        planDAO = new OutdoorActivitiesDAO(latitude, longitude, date);
        violations = validator.validate(planDAO);
        assertFalse(violations.isEmpty());
    }

    @Test
    void ItIsPossibleToRetrieveAndResetValues() {
        OutdoorActivitiesDAO planDAO = new OutdoorActivitiesDAO(latitude, longitude, date);

        assertEquals(planDAO.getLatitude(), latitude);
        assertEquals(planDAO.getLongitude(),longitude);
        assertEquals(planDAO.getPlandate(), date);

        planDAO.setLatitude(89.6);
        planDAO.setLongitude(-2.1);
        planDAO.setPlandate(LocalDate.now().plusDays(4));
    }
    
}
