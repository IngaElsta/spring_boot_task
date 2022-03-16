package com.github.IngaElsta.spring_boot_task.weather;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDateTime;
import java.util.Set;


public class AlertTest {
    private Validator validator;

    @BeforeEach
    public void setup() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void WhenAllValidParametersPassed_thenValidationSucceeds(){
        String alertType = "Yellow Flooding Warning";
        LocalDateTime alertStart = LocalDateTime.now();
        LocalDateTime alertEnds = LocalDateTime.now().plusHours(1);

        Alert alert = new Alert(alertType, alertStart, alertEnds);
        Set<ConstraintViolation<Alert>> violations = validator.validate(alert);
        assertTrue(violations.isEmpty());
    }

    @Test
    void WhenOnlyAlertTypePassed_thenValidationSucceeds(){
        String alertType = "Yellow Flooding Warning";

        Alert alert = new Alert(alertType, null, null);
        Set<ConstraintViolation<Alert>> violations = validator.validate(alert);
        assertTrue(violations.isEmpty());
    }

    @Test
    void WhenAlertTypeAndAlertStartPassed_thenValidationSucceeds(){
        String alertType = "Yellow Flooding Warning";
        LocalDateTime alertStart = LocalDateTime.now();

        Alert alert = new Alert(alertType, alertStart, null);
        Set<ConstraintViolation<Alert>> violations = validator.validate(alert);
        assertTrue(violations.isEmpty());
    }
    
    @Test
    void WhenAlertTypeAndAlertEndPassed_thenValidationSucceeds(){
        String alertType = "Yellow Flooding Warning";
        LocalDateTime alertEnd = LocalDateTime.now();

        Alert alert = new Alert(alertType, null, alertEnd);
        Set<ConstraintViolation<Alert>> violations = validator.validate(alert);
        assertTrue(violations.isEmpty());
    }

    @Test
    void WhenNoParametersPassed_thenValidationFails(){
        Alert alert = new Alert("", null, null);
        Set<ConstraintViolation<Alert>> violations = validator.validate(alert);
        assertFalse(violations.isEmpty());
    }

    @Test
    void WhenAlertTypeNotPassed_thenValidationFails(){
        String alertType = "";
        LocalDateTime alertStart = LocalDateTime.now();
        LocalDateTime alertEnd = LocalDateTime.now().plusHours(1);

        Alert alert = new Alert(alertType, alertStart, alertEnd);
        Set<ConstraintViolation<Alert>> violations = validator.validate(alert);
        assertFalse(violations.isEmpty());
    }
    
    //Those tests bellow are expected to pass only when/if validation to time order added
    @Test
    @Disabled
    void WhenAlertEndBeforeAlertStart_thenValidationFails(){
        String alertType = "Yellow Flooding Warning";
        LocalDateTime alertStart = LocalDateTime.now();
        LocalDateTime alertEnd = LocalDateTime.now().minusHours(1);

        Alert alert = new Alert(alertType, alertStart, alertEnd);
        Set<ConstraintViolation<Alert>> violations = validator.validate(alert);
        assertFalse(violations.isEmpty());
    }

    @Test
    @Disabled
    void WhenAlertEndBeforeStartOfDay_thenValidationFails(){
        String alertType = "Yellow Flooding Warning";
        LocalDateTime alertEnd = LocalDateTime.now().minusHours(1);

        Alert alert = new Alert(alertType, null, alertEnd);
        Set<ConstraintViolation<Alert>> violations = validator.validate(alert);
        assertFalse(violations.isEmpty());
    }

    @Test
    @Disabled
    void WhenalertStartAfterEndOfDay_thenValidationFails(){
        String alertType = "Yellow Flooding Warning";
        LocalDateTime alertEnd = LocalDateTime.now().minusHours(1);

        Alert alert = new Alert(alertType, null, alertEnd);
        Set<ConstraintViolation<Alert>> violations = validator.validate(alert);
        assertFalse(violations.isEmpty());
    }
}
