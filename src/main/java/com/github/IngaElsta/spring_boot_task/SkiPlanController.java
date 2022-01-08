package com.github.IngaElsta.spring_boot_task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping ("api/v1/ski-planner")
public class SkiPlanController {

    private final SkiPlanService skiPlanService;

    @Autowired
    public SkiPlanController(SkiPlanService skiPlanService) {
        this.skiPlanService = skiPlanService;
    }

    @GetMapping ("/weather")
    public List<WeatherConditions> getWeather(
            @RequestParam (value = "lat", required = false, defaultValue = "56.95") String lat,
            @RequestParam (value = "lon", required = false, defaultValue = "24.11") String lon) {
        try {
            Double.valueOf(lat);
            Double.valueOf(lon);

            SkiLocation location = new SkiLocation (lat, lon);
            return skiPlanService.getWeather(location);
        } catch (NumberFormatException e) {
            log.error("GetWeather: Non numeric values were passed as location - lat {}, lon {}", lat, lon);
            //TODO: Add error message instead of return
            return new ArrayList<>();
        }
    }


}
