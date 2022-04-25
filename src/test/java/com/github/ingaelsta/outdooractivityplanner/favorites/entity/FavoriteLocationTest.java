package com.github.ingaelsta.outdooractivityplanner.favorites.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class FavoriteLocationTest {
    private Validator validator;
    private final Double latitude = 52.1;
    private final Double longitude = -0.78;

    @BeforeEach
    public void setup() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void WhenAllDataValid_thenValidationSucceeds(){
        FavoriteLocation favoriteLocation = new FavoriteLocation(latitude, longitude);

        Set<ConstraintViolation<FavoriteLocation>> violations = validator.validate(favoriteLocation);
        assertTrue(violations.isEmpty());
    }

    @Test
    void WhenAnyVariableIsNull_thenValidationFails(){
        FavoriteLocation favoriteLocation = new FavoriteLocation(null, longitude);
        Set<ConstraintViolation<FavoriteLocation>> violations = validator.validate(favoriteLocation);
        assertFalse(violations.isEmpty());

        favoriteLocation = new FavoriteLocation(latitude, null);
        violations = validator.validate(favoriteLocation);
        assertFalse(violations.isEmpty());
    }

    @Test
    void WhenInvalidCoordinatesPassed_thenValidationFails() {
        Double longitude = 1900.9; //larger than max
        FavoriteLocation favoriteLocation = new FavoriteLocation(latitude, longitude);
        Set<ConstraintViolation<FavoriteLocation>> violations = validator.validate(favoriteLocation);
        assertFalse(violations.isEmpty());

        Double latitude = 100.0; //larger than max
        favoriteLocation = new FavoriteLocation(latitude, longitude);
        violations = validator.validate(favoriteLocation);
        assertFalse(violations.isEmpty());
    }

    @Test
    void ItIsPossibleToRetrieveAndResetValues() {
        FavoriteLocation favoriteLocation = new FavoriteLocation(latitude, longitude);

        assertEquals(favoriteLocation.getLatitude(), latitude);
        assertEquals(favoriteLocation.getLongitude(),longitude);

        favoriteLocation.setLatitude(89.6);
        favoriteLocation.setLongitude(-2.1);
    }

}