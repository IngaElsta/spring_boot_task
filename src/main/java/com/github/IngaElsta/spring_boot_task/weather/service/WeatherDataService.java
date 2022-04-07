package com.github.IngaElsta.spring_boot_task.weather.service;

import com.github.IngaElsta.spring_boot_task.planning.domain.SkiLocation;
import com.github.IngaElsta.spring_boot_task.weather.domain.WeatherConditions;

import java.time.LocalDate;
import java.util.Map;

public interface WeatherDataService {
    Map<LocalDate, WeatherConditions> retrieveWeather (SkiLocation location);
}
