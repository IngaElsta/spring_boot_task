package com.github.ingaelsta.outdooractivityplanner.weather.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.ingaelsta.outdooractivityplanner.commons.model.Location;
import com.github.ingaelsta.outdooractivityplanner.weather.configuration.OWMConfiguration;
import com.github.ingaelsta.outdooractivityplanner.weather.configuration.OWMObjectMapperConfiguration;
import com.github.ingaelsta.outdooractivityplanner.weather.model.WeatherConditions;
import com.github.ingaelsta.outdooractivityplanner.weather.exception.OWMDataException;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;

import java.net.URI;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Map;

@Slf4j
@Component
public class OWMDataService implements WeatherDataService {
    private final String owmOneApiUrl = "https://api.openweathermap.org/data/2.5";
    private final ObjectMapper objectMapper;
    private final OWMConfiguration owmConfiguration;

    private final WebClient webClient;

    public OWMDataService(OWMConfiguration owmConfiguration,
                          OWMObjectMapperConfiguration OWMobjectMapperConfiguration) {
        this.owmConfiguration = owmConfiguration;
        this.objectMapper = OWMobjectMapperConfiguration.getObjectMapper();
        this.webClient = WebClient.create(owmOneApiUrl);
    }

    public Map<LocalDate, WeatherConditions> retrieveWeather (Location location) {
        //todo: might try again using the original string from properties file
        //todo: works but seems much slower (15s???)... commit now, investigate later
        //todo: add circuit breaker
        String response = webClient.get()
                .uri(uriBuilder -> buildWithParams(location, uriBuilder))
                .retrieve()
                .onStatus(HttpStatus::isError, throwable -> {
                    throw new OWMDataException("Failed to retrieve weather data");
                })
                .bodyToMono(String.class)
                .block(Duration.of(60000, ChronoUnit.MILLIS));
        return processWeatherData(response, objectMapper);
    }

    private URI buildWithParams(Location location, UriBuilder uriBuilder) {
        return uriBuilder
                .path("/onecall")
                .queryParam("lat", location.getLatitude())
                .queryParam("lon", location.getLongitude())
                .queryParam("exclude", "minutely,hourly,current")
                .queryParam("appid", owmConfiguration.getAuthToken())
                .queryParam("units", "metric")
                .build();
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
