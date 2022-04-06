package com.github.IngaElsta.spring_boot_task.planning.controller;

import com.github.IngaElsta.spring_boot_task.planning.service.SkiPlanService;
import com.github.IngaElsta.spring_boot_task.planning.domain.SkiLocation;
import com.github.IngaElsta.spring_boot_task.weather.domain.WeatherConditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDate;
import java.util.Map;

@Slf4j
@RestController
@Validated
@RequestMapping ("api/v1/ski-planner")
public class SkiPlanController {

    private final SkiPlanService skiPlanService;

    @Autowired
    public SkiPlanController(SkiPlanService skiPlanService) {
        this.skiPlanService = skiPlanService;
    }

    @GetMapping ("/weather")
    public Map<LocalDate, WeatherConditions> getWeather(
            @RequestParam (value = "lat", required = false, defaultValue = "56.95")
            @Min(-90) @Max(90)
                    Double lat,
            @RequestParam (value = "lon", required = false, defaultValue = "24.11")
            @Min(-180) @Max(180)
                    Double lon) {
        try {
            return skiPlanService.getWeather(new SkiLocation(lat, lon));
        } catch (HttpClientErrorException e) {
            log.error("GetWeather: Invalid values were passed for location - lat {}, lon {}", lat, lon);
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Invalid values were passed for location");
        }
    }


}
