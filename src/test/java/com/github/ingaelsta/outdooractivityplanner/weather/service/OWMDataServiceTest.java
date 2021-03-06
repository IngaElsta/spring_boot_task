package com.github.ingaelsta.outdooractivityplanner.weather.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ingaelsta.outdooractivityplanner.commons.model.Location;
import com.github.ingaelsta.outdooractivityplanner.weather.configuration.OWMConfiguration;
import com.github.ingaelsta.outdooractivityplanner.weather.configuration.OWMObjectMapperConfiguration;
import com.github.ingaelsta.outdooractivityplanner.weather.model.Temperature;
import com.github.ingaelsta.outdooractivityplanner.weather.model.WeatherConditions;
import com.github.ingaelsta.outdooractivityplanner.commons.Conversion;
import com.github.ingaelsta.outdooractivityplanner.weather.model.Wind;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestPropertySource(locations="classpath:test.properties") //doesn't work with yaml, might need a workaround
public class OWMDataServiceTest {

    //todo: try checking if correct arguments are passed
//    @Captor
//    private ArgumentCaptor<ClientRequest> argumentCaptor;

    @Autowired
    private OWMConfiguration owmConfiguration;
    private ObjectMapper objectMapperMock = Mockito.mock(ObjectMapper.class);
    private OWMObjectMapperConfiguration objectMapperConfigMock;
    private OWMDataService owmDataServiceMock;
    public static MockWebServer mockBackEnd;

    private Map<LocalDate, WeatherConditions> weatherConditionsMap;
    private Location location;

    @BeforeAll
    static void setUp() throws IOException {
        mockBackEnd = new MockWebServer();
        mockBackEnd.start(8085);
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockBackEnd.shutdown();
    }

    @BeforeEach
    public void setup() {
        {
            LocalDate date = Conversion.convertDate(1643536800).toLocalDate();
            Temperature temperature = new Temperature(1.64, 1.09, -0.16, -0.94);
            Wind wind = new Wind(8.23, 17.56, "S");
            List<String> weatherDescriptions = new ArrayList<>();
            weatherDescriptions.add("rain and snow");
            WeatherConditions conditions = new WeatherConditions(date, weatherDescriptions, temperature, wind, new ArrayList<>());
            weatherConditionsMap = new HashMap<>();
            weatherConditionsMap.put(date, conditions);
            Double latitude = 55.87;
            Double longitude = 26.52;
            location = new Location(latitude, longitude);
        }
        objectMapperConfigMock = new OWMObjectMapperConfiguration(objectMapperMock);
        owmDataServiceMock = new OWMDataService(owmConfiguration, objectMapperConfigMock);
    }

    @Test
    void When_mockReturnsStatusOKAndData_Then_retrieveWeatherReturnsDeserializedData() throws JsonProcessingException {
        String text = "placeholder";
        mockBackEnd.enqueue(new MockResponse()
                .setBody(text)
                .addHeader("Content-Type", "application/json"));
        when(objectMapperMock.readValue(text, Map.class))
                .thenReturn(weatherConditionsMap);
        Map<LocalDate, WeatherConditions> result = owmDataServiceMock.retrieveWeather(location);
        assertEquals(weatherConditionsMap, result);
    }

}
