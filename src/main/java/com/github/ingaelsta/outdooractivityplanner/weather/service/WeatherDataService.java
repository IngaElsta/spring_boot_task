package com.github.ingaelsta.outdooractivityplanner.weather.service;

import com.github.ingaelsta.outdooractivityplanner.planning.entity.OutdoorActivitiesLocation;
import com.github.ingaelsta.outdooractivityplanner.weather.entity.WeatherConditions;

import java.time.LocalDate;
import java.util.Map;

public interface WeatherDataService {
    Map<LocalDate, WeatherConditions> retrieveWeather (OutdoorActivitiesLocation location);
}
