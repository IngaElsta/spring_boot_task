package com.github.IngaElsta.spring_boot_task.weather.service;

import com.github.IngaElsta.spring_boot_task.weather.domain.Temperature;
import com.github.IngaElsta.spring_boot_task.weather.domain.WeatherConditions;
import com.github.IngaElsta.spring_boot_task.weather.domain.Wind;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.nio.file.Files;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

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
        throws IOException, NullPointerException{

        File noAlerts = ResourceUtils.getFile(
                "classpath:single_day_no_alerts.json");
        String text = new String(Files.readAllBytes(noAlerts.toPath()));
        Map<LocalDate, WeatherConditions> result = OWMDataService.processWeatherData(text);

        LocalDate date = WeatherConditions.convertDate(1643536800).toLocalDate();;
        Temperature temperature = new Temperature("1.64", "1.09", "-0.16", "-0.94");
        Wind wind = new Wind("8.23", "17.56", "S");
        WeatherConditions conditions = new WeatherConditions(date, "rain and snow", temperature, wind, null);

        Map<LocalDate, WeatherConditions> expected = new HashMap<>();
        expected.put(date, conditions);

        assertEquals(expected, result);
    }

}
