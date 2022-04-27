package com.github.ingaelsta.outdooractivityplanner.weather.service;

import com.github.ingaelsta.outdooractivityplanner.commons.model.Location;
import com.github.ingaelsta.outdooractivityplanner.favorites.entity.FavoriteLocation;
import com.github.ingaelsta.outdooractivityplanner.favorites.service.FavoriteLocationService;
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

//    @Autowired
//    CacheManager cacheManager;

    private final WeatherDataService weatherDataService;
    private final FavoriteLocationService favoriteLocationService;

    public WeatherService (WeatherDataService weatherDataService,
                           FavoriteLocationService favoriteLocationService) {
        this.weatherDataService = weatherDataService;
        this.favoriteLocationService = favoriteLocationService;
    }

    @Cacheable("weather")
    public Map<LocalDate, WeatherConditions> getWeather (Location location) {
        //todo: fix this and remove printing as doesn't cache from scheduled calls
        Map<LocalDate, WeatherConditions> weatherConditionsMap =
                weatherDataService.retrieveWeather(location);

//        System.out.println(location + " weather service: " + weatherConditionsMap + "\n");
//
//        Map<Location, Object> weatherCache = (Map<Location, Object>)cacheManager
//                .getCache("weather").getNativeCache();
//
//        weatherCache.keySet().stream()
//                .forEach(key -> {
//                    System.out.println(key + " " + weatherCache.get(key));
//                });
        return weatherConditionsMap;
    }

    @Scheduled(cron = "0 * * * * *")
    public void cacheWeatherForFavoriteLocations () {
        //todo: fix this and remove printing
//        System.out.println("Scheduled call");
        List<FavoriteLocation> allFavoriteLocations =  favoriteLocationService.getAllFavorites();
        allFavoriteLocations.forEach(favorite -> {
                    Location location = new Location(favorite.getLatitude(), favorite.getLongitude());
                    this.getWeather(location);
                });
    }

}
