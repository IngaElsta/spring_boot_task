package com.github.IngaElsta.spring_boot_task.weather.service;

import com.github.IngaElsta.spring_boot_task.planning.entity.SkiLocation;
import com.github.IngaElsta.spring_boot_task.weather.entity.WeatherConditions;

import java.time.LocalDate;
import java.util.Map;

public interface WeatherDataService {
    Map<LocalDate, WeatherConditions> retrieveWeather (SkiLocation location);
}
