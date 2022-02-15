package com.github.IngaElsta.spring_boot_task.weather;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.IngaElsta.spring_boot_task.planning.SkiLocation;
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
                .expand(location.getLatitude(), location.getLongitude(), owmConfiguration.getAuthToken());
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        System.out.println(response);

        Map<LocalDate, WeatherConditions> conditionsMap = processWeatherData(response);
        return conditionsMap;
    }

    private Map<LocalDate, WeatherConditions> processWeatherData(ResponseEntity<String> WeatherJson) {
        //TODO: add json processing here
        return new HashMap<>();
    }

    private LocalDateTime convertDate(long date_seconds){
        Instant instant = Instant.ofEpochSecond(date_seconds);
        LocalDateTime result = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
        return result;
    }

}
