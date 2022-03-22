package com.github.IngaElsta.spring_boot_task.planning.service;

import com.github.IngaElsta.spring_boot_task.planning.domain.SkiLocation;
import com.github.IngaElsta.spring_boot_task.weather.domain.WeatherConditions;
import com.github.IngaElsta.spring_boot_task.weather.service.WeatherDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;

@Service
public class SkiPlanService {

    private final WeatherDataService weatherDataService;

    @Autowired
    public SkiPlanService(WeatherDataService weatherDataService) {
        this.weatherDataService = weatherDataService;
    }

    //return weather information for following 7 days
    public Map<LocalDate, WeatherConditions> getWeather (SkiLocation location) {
        //TODO: implement actual connecting to cache and processing
        Map<LocalDate, WeatherConditions> weatherConditionsMap = weatherDataService.retrieveWeather(location);
        return weatherConditionsMap;
    }

}
