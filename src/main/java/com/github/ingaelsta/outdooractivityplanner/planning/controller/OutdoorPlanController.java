package com.github.ingaelsta.outdooractivityplanner.planning.controller;

import com.github.ingaelsta.outdooractivityplanner.planning.entity.OutdoorActivitiesDAO;
import com.github.ingaelsta.outdooractivityplanner.commons.model.Location;
import com.github.ingaelsta.outdooractivityplanner.weather.model.WeatherConditions;
import com.github.ingaelsta.outdooractivityplanner.planning.service.OutdoorPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDate;
import java.util.Map;

@RestController
@Validated
@RequestMapping ("api/v1/outdoor-planner")
public class OutdoorPlanController {

    private final OutdoorPlanService outdoorActivityPlanningService;

    @Autowired
    public OutdoorPlanController(OutdoorPlanService outdoorActivityPlanningService) {
        this.outdoorActivityPlanningService = outdoorActivityPlanningService;
    }

    @GetMapping ("/weather")
    public Map<LocalDate, WeatherConditions> getWeather(
            @RequestParam (value = "lat", required = false, defaultValue = "56.95")
            @Min(-90) @Max(90)
                    Double latitude,
            @RequestParam (value = "lon", required = false, defaultValue = "24.11")
            @Min(-180) @Max(180)
                    Double longitude) {
        return outdoorActivityPlanningService.getWeather(new Location(latitude, longitude));
    }

    @PostMapping (value = "/activity", consumes = MediaType.APPLICATION_JSON_VALUE)
    public OutdoorActivitiesDAO saveOutdoorPlan (
            @Validated
            @RequestBody
            OutdoorActivitiesDAO plan) {
        try {
        return outdoorActivityPlanningService.saveOutdoorPlan(plan);
        } catch (Exception e) {
            System.out.println(e);
            throw e;
        }
    }

}
