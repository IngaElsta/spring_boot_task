package com.github.ingaelsta.outdooractivityplanner.weather.controller;

import com.github.ingaelsta.outdooractivityplanner.commons.Conversion;
import com.github.ingaelsta.outdooractivityplanner.commons.model.Location;
import com.github.ingaelsta.outdooractivityplanner.weather.exception.WeatherDataException;
import com.github.ingaelsta.outdooractivityplanner.weather.model.Temperature;
import com.github.ingaelsta.outdooractivityplanner.weather.model.WeatherConditions;
import com.github.ingaelsta.outdooractivityplanner.weather.model.Wind;
import com.github.ingaelsta.outdooractivityplanner.weather.service.WeatherDataService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class WeatherControllerUnitTest {
    private final WeatherDataService weatherDataServiceMock = Mockito.mock(WeatherDataService .class);

    private WeatherController weatherControllerMock;

    private final Double latitude = 55.87;
    private final Double longitude = 26.52;
    private final Location location = new Location(latitude, longitude);
    private final LocalDate date = Conversion.convertDate(1643536800).toLocalDate();

    @BeforeEach
    public void setup () {
        weatherControllerMock = new WeatherController(weatherDataServiceMock) ;
    }

    @Test
    public void WhenValidLocationPassedToGetWeather_thenReturnsData() {
        Temperature temperature = new Temperature(1.64, 1.09, -0.16, -0.94);
        Wind wind = new Wind(8.23, 17.56, "S");
        List<String> weatherDescriptions = new ArrayList<>();
        weatherDescriptions.add("rain and snow");

        Map<LocalDate, WeatherConditions> expected = new HashMap<>();
        expected.put(date, new WeatherConditions(
                date, weatherDescriptions, temperature, wind, new ArrayList<>()));

        when(weatherDataServiceMock.retrieveWeather(location))
                .thenReturn(expected);

        Map<LocalDate, WeatherConditions> result = weatherControllerMock.getWeather(latitude, longitude);

        assertEquals(expected, result);
    }

    @Test
    public void WhenWeatherDataRetrievalUnsuccessful_thenReturnsWeatherDataException() {
        when(weatherDataServiceMock.retrieveWeather(new Location(55.87, 26.52)))
                .thenThrow(new WeatherDataException("placeholder") {});

        assertThrows(WeatherDataException.class, () -> weatherControllerMock.getWeather(55.87, 26.52));
    }

}