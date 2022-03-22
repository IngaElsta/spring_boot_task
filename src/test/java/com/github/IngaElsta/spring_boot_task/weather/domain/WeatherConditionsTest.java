package com.github.IngaElsta.spring_boot_task.weather.domain;

import com.github.IngaElsta.spring_boot_task.weather.domain.Alert;
import com.github.IngaElsta.spring_boot_task.weather.domain.Temperature;
import com.github.IngaElsta.spring_boot_task.weather.domain.WeatherConditions;
import com.github.IngaElsta.spring_boot_task.weather.domain.Wind;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class WeatherConditionsTest {

    private Alert alert1 = new Alert("Yellow Flooding Warning",
            LocalDateTime.now(),
            LocalDateTime.now().plusHours(1));
    private Alert alert2 = new Alert("Red Wind Warning",
            LocalDateTime.now(),
            LocalDateTime.now().plusHours(1));

    private Temperature temperature = new Temperature( "-3", "2", "0", "-5");
    private Wind wind = new Wind("2", "7", "S");
    private List alerts;

    private List weatherDescriptions;
    private Validator validator;

    @BeforeEach
    public void setup() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();

        alerts = new ArrayList<>();
        alerts.add(alert1);
        alerts.add(alert2);

        weatherDescriptions = new ArrayList<>();
        weatherDescriptions.add("rain and snow");
    }

    @Test
    void WhenAcceptableValuesWithoutAlertsPassed_thenValidationSucceeds(){
        WeatherConditions conditions = new WeatherConditions(LocalDate.now(), weatherDescriptions, temperature, wind, null);
        Set<ConstraintViolation<WeatherConditions>> violations = validator.validate(conditions);
        assertTrue(violations.isEmpty());
    }

    @Test
    void WhenAcceptableValuesWithAlertsPassed_thenValidationSucceeds(){
        WeatherConditions conditions = new WeatherConditions(LocalDate.now(), weatherDescriptions, temperature, wind, alerts);
        Set<ConstraintViolation<WeatherConditions>> violations = validator.validate(conditions);
        assertTrue(violations.isEmpty());
    }

    @Test
    void WhenWeatherDescriptionIsNull_thenValidationFails(){
        WeatherConditions conditions = new WeatherConditions(LocalDate.now(), null, temperature, wind, alerts);
        Set<ConstraintViolation<WeatherConditions>> violations = validator.validate(conditions);
        assertFalse(violations.isEmpty());
    }

    @Test
    void WhenWeatherDescriptionIsEmpty_thenValidationFails(){
        WeatherConditions conditions = new WeatherConditions(LocalDate.now(), new ArrayList<>(), temperature, wind, alerts);
        Set<ConstraintViolation<WeatherConditions>> violations = validator.validate(conditions);
        assertFalse(violations.isEmpty());
    }

    @Test
    void WhenDateIsNull_thenValidationFails(){
        WeatherConditions conditions = new WeatherConditions(null, weatherDescriptions, temperature, wind, alerts);
        Set<ConstraintViolation<WeatherConditions>> violations = validator.validate(conditions);
        assertFalse(violations.isEmpty());
    }

    @Test
    void WhenTemperatureIsNull_thenValidationFails(){
        WeatherConditions conditions = new WeatherConditions(LocalDate.now(), weatherDescriptions, null, wind, alerts);
        Set<ConstraintViolation<WeatherConditions>> violations = validator.validate(conditions);
        assertFalse(violations.isEmpty());
    }

    @Test
    void WhenWindIsNull_thenValidationFails(){
        WeatherConditions conditions = new WeatherConditions(LocalDate.now(), weatherDescriptions, temperature, null, alerts);
        Set<ConstraintViolation<WeatherConditions>> violations = validator.validate(conditions);
        assertFalse(violations.isEmpty());
    }

    //Those tests bellow are expected to pass only when/if validation of date check is added
    @Disabled
    @Test
    void WhenAlertEndsBeforeStartOfDayIsNull_thenValidationFails(){
        WeatherConditions conditions = new WeatherConditions(LocalDate.now().plusDays(2), weatherDescriptions, temperature, wind, alerts);
        Set<ConstraintViolation<WeatherConditions>> violations = validator.validate(conditions);
        assertFalse(violations.isEmpty());
    }

    @Disabled
    @Test
    void WhenAlertStartsBeforeEndOfDayIsNull_thenValidationFails(){
        WeatherConditions conditions = new WeatherConditions(LocalDate.now().minusDays(2), weatherDescriptions, temperature, wind, alerts);
        Set<ConstraintViolation<WeatherConditions>> violations = validator.validate(conditions);
        assertFalse(violations.isEmpty());
    }

}