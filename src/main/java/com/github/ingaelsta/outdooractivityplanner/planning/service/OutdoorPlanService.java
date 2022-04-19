package com.github.ingaelsta.outdooractivityplanner.planning.service;

import com.github.ingaelsta.outdooractivityplanner.planning.model.OutdoorActivitiesLocation;
import com.github.ingaelsta.outdooractivityplanner.weather.entity.WeatherConditions;
import com.github.ingaelsta.outdooractivityplanner.weather.service.WeatherDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;

@Service
public class OutdoorPlanService {

    private final WeatherDataService weatherDataService;

    @Autowired
    public OutdoorPlanService(WeatherDataService weatherDataService) {
        this.weatherDataService = weatherDataService;
    }

    //return weather information for following 7 days
    public Map<LocalDate, WeatherConditions> getWeather (OutdoorActivitiesLocation location) {
        //TODO: implement actual connecting to cache and processing
        Map<LocalDate, WeatherConditions> weatherConditionsMap = weatherDataService.retrieveWeather(location);
        return weatherConditionsMap;
    }

}