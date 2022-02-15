package com.github.IngaElsta.spring_boot_task.planning;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

public class SkiLocationTest {

    private Validator validator;

    @BeforeEach
    public void setup() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void WhenCoordinatesAreAcceptable_thenValidationSucceeds(){
        String latitude = "52.1";
        String longitude = "-0.78";
        SkiLocation location = new SkiLocation(latitude, longitude);
        Set<ConstraintViolation<SkiLocation>> violations = validator.validate(location);
        assertTrue(violations.isEmpty());

    }

    @Test
    void WhenLongitudeNotPassed_thenValidationFails(){
        String latitude = "56.95";

        SkiLocation location = new SkiLocation(latitude, "");
        Set<ConstraintViolation<SkiLocation>> violations = validator.validate(location);
        assertFalse(violations.isEmpty());
    }

    @Test
    void WhenLatitudeNotPassed_thenValidationFails(){
        String longitude = "24.11";

        SkiLocation location = new SkiLocation(null, longitude);
        Set<ConstraintViolation<SkiLocation>> violations = validator.validate(location);
        assertFalse(violations.isEmpty());
    }

    @Test
    void WhenNonDigitsNotPassed_thenValidationFails(){
        String latitude = "a";
        String longitude = "2n";

        SkiLocation location = new SkiLocation(latitude, longitude);
        Set<ConstraintViolation<SkiLocation>> violations = validator.validate(location);
        assertFalse(violations.isEmpty());
    }

    @Test
    void WhenInvalidLongitudePassed_thenValidationFails() {
        String latitude = "80"; //valid
        String longitude = "181"; //larger than max
        SkiLocation location = new SkiLocation(latitude, longitude);
        Set<ConstraintViolation<SkiLocation>> violations = validator.validate(location);
        assertFalse(violations.isEmpty());

        longitude = "0.00001"; //too long fraction
        location = new SkiLocation(latitude, longitude);
        violations = validator.validate(location);
        assertFalse(violations.isEmpty());
    }

    @Test
    void WhenInvalidLatitudePassed_thenValidationFails() {
        String latitude = "100"; //both too long and larger than max
        String longitude = "-179";
        SkiLocation location = new SkiLocation(latitude, longitude);
        Set<ConstraintViolation<SkiLocation>> violations = validator.validate(location);
        assertFalse(violations.isEmpty());


        latitude = "-0.00001"; //too long fraction
        location = new SkiLocation(latitude, longitude);
        violations = validator.validate(location);
        assertFalse(violations.isEmpty());
    }

}
