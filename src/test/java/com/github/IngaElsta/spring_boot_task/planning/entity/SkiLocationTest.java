package com.github.IngaElsta.spring_boot_task.planning.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

public class SkiLocationTest {

    private final Double latitude = 52.1;
    private final Double longitude = -0.78;

    private Validator validator;

    @BeforeEach
    public void setup() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void WhenCoordinatesAreAcceptable_thenValidationSucceeds(){
        SkiLocation location = new SkiLocation(latitude, longitude);
        Set<ConstraintViolation<SkiLocation>> violations = validator.validate(location);
        assertTrue(violations.isEmpty());

    }

    @Test
    void WhenLongitudeNotPassed_thenValidationFails(){

        SkiLocation location = new SkiLocation(latitude, null);
        Set<ConstraintViolation<SkiLocation>> violations = validator.validate(location);
        assertFalse(violations.isEmpty());
    }

    @Test
    void WhenLatitudeNotPassed_thenValidationFails(){

        SkiLocation location = new SkiLocation(null, longitude);
        Set<ConstraintViolation<SkiLocation>> violations = validator.validate(location);
        assertFalse(violations.isEmpty());
    }

    @Test
    void WhenInvalidLongitudePassed_thenValidationFails() {
        Double longitude = 1900.9; //larger than max
        SkiLocation location = new SkiLocation(latitude, longitude);
        Set<ConstraintViolation<SkiLocation>> violations = validator.validate(location);
        assertFalse(violations.isEmpty());
    }

    @Test
    void WhenInvalidLatitudePassed_thenValidationFails() {
        Double latitude = 100.0; //larger than max
        SkiLocation location = new SkiLocation(latitude, longitude);
        Set<ConstraintViolation<SkiLocation>> violations = validator.validate(location);
        assertFalse(violations.isEmpty());
    }

    @Test
    void ItIsPossibleToRetrieveAndResetAnyValue() {
        SkiLocation location = new SkiLocation(latitude, longitude);

        assertEquals(location.getLatitude(), latitude);
        assertEquals(location.getLongitude(), longitude);

        location.setLatitude(50.0);
        location.setLongitude(20.1);
    }

}
