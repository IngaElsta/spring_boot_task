package com.github.IngaElsta.spring_boot_task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class SkiPlanService {

    private final WeatherDataService weatherDataService;

    @Autowired
    public SkiPlanService(WeatherDataService weatherDataService) {
        this.weatherDataService = weatherDataService;
    }

    //return weather information for following 7 days
    public List<WeatherConditions> getWeather (SkiLocation location) {
        //TODO: implement actual connecting to cache and processing
        weatherDataService.retrieveWeather(location);
        return new ArrayList<>();
    }

}
