package com.github.IngaElsta.spring_boot_task.planning.controller;

import com.github.IngaElsta.spring_boot_task.planning.service.SkiPlanService;
import com.github.IngaElsta.spring_boot_task.planning.domain.SkiLocation;
import com.github.IngaElsta.spring_boot_task.weather.domain.WeatherConditions;
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
        return skiPlanService.getWeather(new SkiLocation(lat, lon));
    }


}
