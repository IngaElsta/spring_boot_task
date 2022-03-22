package com.github.IngaElsta.spring_boot_task.weather.service;

import com.github.IngaElsta.spring_boot_task.planning.domain.SkiLocation;
import com.github.IngaElsta.spring_boot_task.weather.configuration.OWMConfiguration;
import com.github.IngaElsta.spring_boot_task.weather.domain.WeatherConditions;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplate;

import java.time.*;
import java.util.*;

@Component
public class OWMDataService implements WeatherDataService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final OWMConfiguration owmConfiguration;

    @Autowired
    public OWMDataService(RestTemplateBuilder restTemplateBuilder, ObjectMapper objectMapper,
                              OWMConfiguration owmConfiguration) {
        this.restTemplate = restTemplateBuilder.build();
        this.objectMapper = objectMapper;
        this.owmConfiguration = owmConfiguration;
    }


    public Map<LocalDate, WeatherConditions> retrieveWeather (SkiLocation location) {
        var url = new UriTemplate(owmConfiguration.getOneApiUrl())
                .expand(location.getLatitude(), location.getLongitude(),
                        owmConfiguration.getAuthToken());
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        System.out.println(response);

        if (response.getStatusCodeValue() >= 200 && response.getStatusCodeValue() < 400) {
            Map<LocalDate, WeatherConditions> conditionsMap = processWeatherData(
                    response.getBody());
            return conditionsMap;
        } else {
            //todo: refactor the code to process error responses
            return new HashMap<>();
        }
    }

    //might make this private again later
    public static Map<LocalDate, WeatherConditions> processWeatherData(String weatherJson) {
        //TODO: add json processing here
        return new HashMap<>();
    }

    private LocalDateTime convertDate(long date_seconds){
        Instant instant = Instant.ofEpochSecond(date_seconds);
        return instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

}
