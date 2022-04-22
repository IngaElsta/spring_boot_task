package com.github.ingaelsta.outdooractivityplanner.weather.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ingaelsta.outdooractivityplanner.weather.model.Temperature;
import com.github.ingaelsta.outdooractivityplanner.weather.model.WeatherConditions;
import com.github.ingaelsta.outdooractivityplanner.commons.Conversion;
import com.github.ingaelsta.outdooractivityplanner.weather.model.Wind;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class OWMDataServiceTest {

    @Mock
    RestTemplate restTemplate;

    @InjectMocks
    @Spy
    OWMDataService owmDataService;
    ResponseEntity responseEntity = mock(ResponseEntity.class);

    @BeforeEach
    public void setup() {

    }

    @Test
    void When_data_valid_with_no_alerts_then_conversion_succeeds ()
            throws IOException {

        ObjectMapper mapper = Mockito.mock(ObjectMapper.class);

        LocalDate date = Conversion.convertDate(1643536800).toLocalDate();
        Temperature temperature = new Temperature(1.64, 1.09, -0.16, -0.94);
        Wind wind = new Wind(8.23, 17.56, "S");
        List<String> weatherDescriptions = new ArrayList<>();
        weatherDescriptions.add("rain and snow");

        WeatherConditions conditions = new WeatherConditions(date, weatherDescriptions, temperature, wind, new ArrayList<>());
        Map<LocalDate, WeatherConditions> expected = new HashMap<>();
        expected.put(date, conditions);

        String text = "placeholder";
        doReturn(expected).when(mapper).readValue(text, Map.class);

        Map<LocalDate, WeatherConditions> result = OWMDataService.processWeatherData(text, mapper);

        assertEquals(expected, result);
    }

}
