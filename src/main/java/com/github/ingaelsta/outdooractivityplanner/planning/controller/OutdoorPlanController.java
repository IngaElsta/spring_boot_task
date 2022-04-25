package com.github.ingaelsta.outdooractivityplanner.planning.controller;

import com.github.ingaelsta.outdooractivityplanner.planning.entity.OutdoorActivity;
import com.github.ingaelsta.outdooractivityplanner.commons.model.Location;
import com.github.ingaelsta.outdooractivityplanner.planning.response.OutdoorPlanResponse;
import com.github.ingaelsta.outdooractivityplanner.weather.model.WeatherConditions;
import com.github.ingaelsta.outdooractivityplanner.planning.service.OutdoorPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDate;
import java.util.Map;

@RestController
@Validated
@RequestMapping ("api/v1/outdoor-planner")
public class OutdoorPlanController {

    private final OutdoorPlanService outdoorPlanService;

    @Autowired
    public OutdoorPlanController(OutdoorPlanService outdoorPlanService) {
        this.outdoorPlanService = outdoorPlanService;
    }

    @GetMapping ("/weather")
    public Map<LocalDate, WeatherConditions> getWeather(
            @RequestParam (value = "lat", required = false, defaultValue = "56.95")
            @Min(-90) @Max(90)
                    Double latitude,
            @RequestParam (value = "lon", required = false, defaultValue = "24.11")
            @Min(-180) @Max(180)
                    Double longitude) {
        return outdoorPlanService.getWeather(new Location(latitude, longitude));
    }

    @PostMapping (value = "/activity", consumes = MediaType.APPLICATION_JSON_VALUE)
    public OutdoorPlanResponse saveOutdoorPlan (
            @Validated
            @RequestBody
            OutdoorActivity plan) {
        return outdoorPlanService.saveOutdoorPlan(plan);
    }

    @PostMapping (value = "/safeactivity", consumes = MediaType.APPLICATION_JSON_VALUE)
    public OutdoorPlanResponse saveSafeOutdoorPlan (
            @Validated
            @RequestBody
            OutdoorActivity plan) {
        return outdoorPlanService.saveSafeOutdoorPlan(plan);
    }

}
