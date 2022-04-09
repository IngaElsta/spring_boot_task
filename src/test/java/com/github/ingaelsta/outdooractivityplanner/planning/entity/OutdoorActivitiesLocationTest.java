package com.github.ingaelsta.outdooractivityplanner.planning.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

public class OutdoorActivitiesLocationTest {

    private final Double latitude = 52.1;
    private final Double longitude = -0.78;

    private Validator validator;

    @BeforeEach
    public void setup() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void WhenCoordinatesAreAcceptable_thenValidationSucceeds(){
        OutdoorActivitiesLocation location = new OutdoorActivitiesLocation(latitude, longitude);
        Set<ConstraintViolation<OutdoorActivitiesLocation>> violations = validator.validate(location);
        assertTrue(violations.isEmpty());

    }

    @Test
    void WhenLongitudeNotPassed_thenValidationFails(){

        OutdoorActivitiesLocation location = new OutdoorActivitiesLocation(latitude, null);
        Set<ConstraintViolation<OutdoorActivitiesLocation>> violations = validator.validate(location);
        assertFalse(violations.isEmpty());
    }

    @Test
    void WhenLatitudeNotPassed_thenValidationFails(){

        OutdoorActivitiesLocation location = new OutdoorActivitiesLocation(null, longitude);
        Set<ConstraintViolation<OutdoorActivitiesLocation>> violations = validator.validate(location);
        assertFalse(violations.isEmpty());
    }

    @Test
    void WhenInvalidLongitudePassed_thenValidationFails() {
        Double longitude = 1900.9; //larger than max
        OutdoorActivitiesLocation location = new OutdoorActivitiesLocation(latitude, longitude);
        Set<ConstraintViolation<OutdoorActivitiesLocation>> violations = validator.validate(location);
        assertFalse(violations.isEmpty());
    }

    @Test
    void WhenInvalidLatitudePassed_thenValidationFails() {
        Double latitude = 100.0; //larger than max
        OutdoorActivitiesLocation location = new OutdoorActivitiesLocation(latitude, longitude);
        Set<ConstraintViolation<OutdoorActivitiesLocation>> violations = validator.validate(location);
        assertFalse(violations.isEmpty());
    }

    @Test
    void ItIsPossibleToRetrieveAndResetAnyValue() {
        OutdoorActivitiesLocation location = new OutdoorActivitiesLocation(latitude, longitude);

        assertEquals(location.getLatitude(), latitude);
        assertEquals(location.getLongitude(), longitude);

        location.setLatitude(50.0);
        location.setLongitude(20.1);
    }

}
