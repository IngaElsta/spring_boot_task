package com.github.IngaElsta.spring_boot_task.planning.service;

import com.github.IngaElsta.spring_boot_task.planning.entity.SkiLocation;
import com.github.IngaElsta.spring_boot_task.weather.entity.Temperature;
import com.github.IngaElsta.spring_boot_task.weather.entity.WeatherConditions;
import com.github.IngaElsta.spring_boot_task.weather.entity.Wind;
import com.github.IngaElsta.spring_boot_task.weather.exception.WeatherDataException;
import com.github.IngaElsta.spring_boot_task.weather.service.WeatherDataService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class SkiPlanServiceTest {

    private WeatherDataService weatherDataServiceMock = Mockito.mock(WeatherDataService.class);

    private SkiPlanService skiPlanServiceMock;

    @BeforeEach
    public void setup() {
        skiPlanServiceMock = new SkiPlanService(weatherDataServiceMock);
    }

    @Test
    public void WhenWeatherDataProcessedSuccessfully_GetWeatherShouldReturnData() throws Exception {
        LocalDate date = WeatherConditions.convertDate(1643536800).toLocalDate();
        Temperature temperature = new Temperature(1.64, 1.09, -0.16, -0.94);
        Wind wind = new Wind(8.23, 17.56, "S");
        List<String> weatherDescriptions = new ArrayList<>();
        weatherDescriptions.add("rain and snow");

        Map<LocalDate, WeatherConditions> expected = new HashMap<>();
        expected.put(date, new WeatherConditions(
                date, weatherDescriptions, temperature, wind, new ArrayList<>()));
        SkiLocation location = new SkiLocation(55.87, 26.52);

        when(weatherDataServiceMock.retrieveWeather(location)).thenReturn(expected);

        Map<LocalDate, WeatherConditions> result = skiPlanServiceMock.getWeather(location);

        assertEquals(expected, result);
    }

    @Test
    public void WhenWeatherDataProcessingHasFailed_GetWeatherShouldThrowException() throws Exception {
        SkiLocation location = new SkiLocation(55.87, 26.52);

        when(weatherDataServiceMock.retrieveWeather(location))
                .thenThrow(new WeatherDataException("Failed") {
        });

        assertThrows(WeatherDataException.class, () -> {skiPlanServiceMock.getWeather(location);});
    }

}