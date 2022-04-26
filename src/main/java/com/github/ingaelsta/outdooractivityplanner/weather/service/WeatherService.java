package com.github.ingaelsta.outdooractivityplanner.weather.service;

import com.github.ingaelsta.outdooractivityplanner.commons.model.Location;
import com.github.ingaelsta.outdooractivityplanner.favorites.service.FavoriteLocationService;
import com.github.ingaelsta.outdooractivityplanner.weather.model.WeatherConditions;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;

@Service
public class WeatherService {

    private final WeatherDataService weatherDataService;
    private final FavoriteLocationService favoriteLocationService;

    public WeatherService (WeatherDataService weatherDataService,
                           FavoriteLocationService favoriteLocationService) {
        this.weatherDataService = weatherDataService;
        this.favoriteLocationService = favoriteLocationService;
    }

    public Map<LocalDate, WeatherConditions> getWeather (Location location) {
        Map<LocalDate, WeatherConditions> weatherConditionsMap =
                weatherDataService.retrieveWeather(location);
        return weatherConditionsMap;
    }

}
