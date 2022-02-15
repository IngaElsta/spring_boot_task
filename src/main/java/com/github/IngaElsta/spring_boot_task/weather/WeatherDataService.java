package com.github.IngaElsta.spring_boot_task.weather;

import com.github.IngaElsta.spring_boot_task.planning.SkiLocation;

import java.time.LocalDate;
import java.util.Map;

public interface WeatherDataService {
    Map<LocalDate, WeatherConditions> retrieveWeather (SkiLocation location);
}
