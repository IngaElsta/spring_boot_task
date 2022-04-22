package com.github.ingaelsta.outdooractivityplanner.weather.service;

import com.github.ingaelsta.outdooractivityplanner.planning.model.OutdoorLocation;
import com.github.ingaelsta.outdooractivityplanner.weather.model.WeatherConditions;

import java.time.LocalDate;
import java.util.Map;

public interface WeatherDataService {
    Map<LocalDate, WeatherConditions> retrieveWeather (OutdoorLocation location);
}
