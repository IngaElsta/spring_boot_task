package com.github.ingaelsta.outdooractivityplanner.weather.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.ingaelsta.outdooractivityplanner.planning.entity.OutdoorActivitiesLocation;
import com.github.ingaelsta.outdooractivityplanner.weather.configuration.OWMConfiguration;
import com.github.ingaelsta.outdooractivityplanner.weather.deserialize.OWMDeserializer;
import com.github.ingaelsta.outdooractivityplanner.weather.entity.WeatherConditions;
import com.github.ingaelsta.outdooractivityplanner.weather.exception.OWMDataException;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplate;

import java.time.*;
import java.util.*;

@Slf4j
@Component
public class OWMDataService implements WeatherDataService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final OWMConfiguration owmConfiguration;

    @Autowired
    public OWMDataService(RestTemplateBuilder restTemplateBuilder, ObjectMapper objectMapper,
                          OWMConfiguration owmConfiguration) {
        this.restTemplate = restTemplateBuilder.build();
        this.owmConfiguration = owmConfiguration;

        //todo: move this to a separate config class?
        this.objectMapper = objectMapper;
        SimpleModule module = new SimpleModule("OWMDeserializer",
                new Version(1, 0, 0, null, null, null));
        module.addDeserializer(Map.class, new OWMDeserializer());
        System.out.println(module);
        objectMapper.registerModule(module);
    }


    public Map<LocalDate, WeatherConditions> retrieveWeather (OutdoorActivitiesLocation location) {
        var url = new UriTemplate(owmConfiguration.getOneApiUrl())
                .expand(location.getLatitude(), location.getLongitude(),
                        owmConfiguration.getAuthToken());
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        //todo: manage error responses properly
        if (response.getStatusCodeValue() >= 200 && response.getStatusCodeValue() < 400) {
            return processWeatherData(
                    response.getBody(), objectMapper);
        } else {
            throw new OWMDataException("Failed to retrieve weather data");
        }
    }

    //todo: probably integrate it back into retrieve weather method
    public static Map<LocalDate, WeatherConditions> processWeatherData(
            String weatherJson,
            ObjectMapper objectMapper) {
        try {
            return objectMapper.readValue(weatherJson, Map.class);
        } catch (JsonProcessingException e) {
            log.error("processWeatherData: Failed to process weather data {}", weatherJson);
            throw new OWMDataException("Failed to process weather data received from OWM");
        }
    }

}
