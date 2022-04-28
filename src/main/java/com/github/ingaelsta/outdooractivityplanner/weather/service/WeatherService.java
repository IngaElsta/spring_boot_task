package com.github.ingaelsta.outdooractivityplanner.weather.service;

import com.github.ingaelsta.outdooractivityplanner.commons.model.Location;
import com.github.ingaelsta.outdooractivityplanner.weather.model.WeatherConditions;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;

@Service
@EnableScheduling
@EnableCaching
public class WeatherService {

    private final WeatherDataService weatherDataService;

    public WeatherService (WeatherDataService weatherDataService) {
        this.weatherDataService = weatherDataService;
    }

    @Cacheable("weather")
    public Map<LocalDate, WeatherConditions> getWeather (Location location) {

        return weatherDataService.retrieveWeather(location);
    }


}
