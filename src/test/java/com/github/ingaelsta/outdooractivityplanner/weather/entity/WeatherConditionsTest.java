package com.github.ingaelsta.outdooractivityplanner.weather.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class WeatherConditionsTest {

    private Alert alert1 = new Alert("Yellow Flooding Warning",
            LocalDateTime.now(),
            LocalDateTime.now().plusHours(1));
    private Alert alert2 = new Alert("Red Wind Warning",
            LocalDateTime.now(),
            LocalDateTime.now().plusHours(1));

    private Temperature temperature = new Temperature( -3.0, 2.0, 0.2, -5.6);
    private Wind wind = new Wind(2.1, 7.4, "S");
    private List alerts;

    private List weatherDescriptions = new ArrayList<>();
    private Validator validator;

    @BeforeEach
    public void setup() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();

        alerts = new ArrayList<>();
        alerts.add(alert1);
        alerts.add(alert2);

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
    void WhenWeatherDescriptionListIsNullOrEmpty_thenValidationFails(){
        WeatherConditions conditions = new WeatherConditions(LocalDate.now(), null, temperature, wind, alerts);
        Set<ConstraintViolation<WeatherConditions>> violations = validator.validate(conditions);
        assertFalse(violations.isEmpty());

        conditions = new WeatherConditions(LocalDate.now(), new ArrayList<>(), temperature, wind, alerts);
        violations = validator.validate(conditions);
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

    @Test
    void ItIsPossibleToRetrieveOrUpdateAnyValues() {
        LocalDate date = LocalDate.now();
        WeatherConditions conditions = new WeatherConditions(date, weatherDescriptions, temperature, wind, alerts);

        assertEquals(conditions.getDate(), date);
        assertEquals(conditions.getAlerts(), alerts);
        assertEquals(conditions.getTemperature(), temperature);
        assertEquals(conditions.getWind(), wind);
        assertEquals(conditions.getWeatherDescriptions(),weatherDescriptions);

        conditions.setDate(date.minusDays(1));
        conditions.setAlerts(null);
        conditions.setTemperature(new Temperature(1.1, 2.3, 0.0, -6.9));
        conditions.setWind(new Wind(10.5,16.7, "E"));
        weatherDescriptions.clear();
        weatherDescriptions.add("clouds");
        conditions.setWeatherDescriptions(weatherDescriptions);
    }

}