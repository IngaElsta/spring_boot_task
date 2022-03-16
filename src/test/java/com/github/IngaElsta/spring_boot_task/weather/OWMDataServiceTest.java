package com.github.IngaElsta.spring_boot_task.weather;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
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

        Temperature temperature = new Temperature("1.64", "1.09", "-0.16");
        Wind wind = new Wind("8.23", "17.56", "S");
        WeatherConditions conditions = new WeatherConditions("rain and snow", temperature, wind, null);
        LocalDate date = Instant.ofEpochSecond(1643536800).atZone(ZoneId.systemDefault()).toLocalDate();

        Map<LocalDate, WeatherConditions> expected = new HashMap<>();
        expected.put(date, conditions);

        assertEquals(expected, result);
    }

}
