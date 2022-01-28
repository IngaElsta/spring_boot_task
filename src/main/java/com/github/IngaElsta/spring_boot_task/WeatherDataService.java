package com.github.IngaElsta.spring_boot_task;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplate;

import java.util.ArrayList;
import java.util.List;

@Component
public class WeatherDataService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final OWMConfiguration owmConfiguration;

    @Autowired
    public WeatherDataService(RestTemplateBuilder restTemplateBuilder, ObjectMapper objectMapper,
                              OWMConfiguration owmConfiguration) {
        this.restTemplate = restTemplateBuilder.build();
        this.objectMapper = objectMapper;
        this.owmConfiguration = owmConfiguration;
    }


    public List<WeatherConditions> retrieveWeather (SkiLocation location) {
        var url = new UriTemplate(owmConfiguration.getOneApiUrl())
                .expand(location.getLatitude(), location.getLongitude(), owmConfiguration.getAuthToken());
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        //TODO: add json processing

        return new ArrayList<>();
    }

}
