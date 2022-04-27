package com.github.ingaelsta.outdooractivityplanner.weather.service;

import com.github.ingaelsta.outdooractivityplanner.commons.model.Location;
import com.github.ingaelsta.outdooractivityplanner.favorites.entity.FavoriteLocation;
import com.github.ingaelsta.outdooractivityplanner.favorites.service.FavoriteLocationService;
import com.github.ingaelsta.outdooractivityplanner.weather.configuration.CachingConfig;
import com.github.ingaelsta.outdooractivityplanner.weather.model.WeatherConditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
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
