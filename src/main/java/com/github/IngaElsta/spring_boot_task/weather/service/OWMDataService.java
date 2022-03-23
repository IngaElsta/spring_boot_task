package com.github.IngaElsta.spring_boot_task.weather.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.IngaElsta.spring_boot_task.planning.domain.SkiLocation;
import com.github.IngaElsta.spring_boot_task.weather.configuration.OWMConfiguration;
import com.github.IngaElsta.spring_boot_task.weather.deserialize.OWMDeserializer;
import com.github.IngaElsta.spring_boot_task.weather.domain.WeatherConditions;

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

        this.objectMapper = objectMapper;
        SimpleModule module = new SimpleModule("OWMDeserializer",
                new Version(1, 0, 0, null, null, null));
        module.addDeserializer(Map.class, new OWMDeserializer());
        System.out.println(module);
        objectMapper.registerModule(module);
    }


    public Map<LocalDate, WeatherConditions> retrieveWeather (SkiLocation location) {
        var url = new UriTemplate(owmConfiguration.getOneApiUrl())
                .expand(location.getLatitude(), location.getLongitude(),
                        owmConfiguration.getAuthToken());
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        //todo: remove this
        System.out.println(response);

        if (response.getStatusCodeValue() >= 200 && response.getStatusCodeValue() < 400) {
            return processWeatherData(
                    response.getBody(), objectMapper);
        } else {
            //todo: refactor the code to process error responses
            return new HashMap<>();
        }
    }

    //might make this private again later
    public static Map<LocalDate, WeatherConditions> processWeatherData(
            String weatherJson,
            ObjectMapper objectMapper) {
        Map weatherConditionsMap = new HashMap<>();
        try {
            weatherConditionsMap = objectMapper.readValue(weatherJson, Map.class);
        } catch (JsonProcessingException e) {
            log.error("processWeatherData: Failed to process weather data {}", weatherJson);
            //todo:throw something
        }

        return weatherConditionsMap;
    }

}
