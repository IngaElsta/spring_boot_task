package com.github.ingaelsta.outdooractivityplanner.weather.controller;

import com.github.ingaelsta.outdooractivityplanner.commons.model.Location;
import com.github.ingaelsta.outdooractivityplanner.weather.model.WeatherConditions;
import com.github.ingaelsta.outdooractivityplanner.weather.service.WeatherDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDate;
import java.util.Map;

@RestController
@Validated
@RequestMapping("api/v1/outdoor-planner/weather")
public class WeatherController {

    private final WeatherDataService weatherDataService;

    @Autowired
    public WeatherController(WeatherDataService weatherDataService) {
        this.weatherDataService = weatherDataService;
    }

    @GetMapping
    public Map<LocalDate, WeatherConditions> getWeather(
            @RequestParam(value = "lat", required = false, defaultValue = "56.95")
            @Min(-90) @Max(90)
            Double latitude,
            @RequestParam (value = "lon", required = false, defaultValue = "24.11")
            @Min(-180) @Max(180)
            Double longitude) {
        return weatherDataService.retrieveWeather(new Location(latitude, longitude));
    }
}
