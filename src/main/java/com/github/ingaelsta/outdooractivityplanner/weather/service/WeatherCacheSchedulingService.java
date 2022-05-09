package com.github.ingaelsta.outdooractivityplanner.weather.service;

import com.github.ingaelsta.outdooractivityplanner.commons.model.Location;
import com.github.ingaelsta.outdooractivityplanner.favorites.entity.FavoriteLocation;
import com.github.ingaelsta.outdooractivityplanner.favorites.service.FavoriteLocationService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WeatherCacheSchedulingService {

    private final WeatherService weatherService;

    //todo: might be better to call favorite location controller API instead of service and make those functionalities independent
    private final FavoriteLocationService favoriteLocationService;


    public WeatherCacheSchedulingService(WeatherService weatherService, FavoriteLocationService favoriteLocationService) {
        this.weatherService = weatherService;
        this.favoriteLocationService = favoriteLocationService;
    }

    @Scheduled(cron = "0 * * * * *")
    public void cacheWeatherForFavoriteLocations () {
        List<FavoriteLocation> allFavoriteLocations =  favoriteLocationService.getAllFavorites();
        allFavoriteLocations
                .forEach(favorite -> {
                    Location location = new Location(favorite.getLatitude(), favorite.getLongitude());
                    weatherService.getWeather(location);
                });
    }
}
