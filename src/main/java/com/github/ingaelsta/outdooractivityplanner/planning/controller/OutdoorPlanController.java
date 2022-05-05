package com.github.ingaelsta.outdooractivityplanner.planning.controller;

import com.github.ingaelsta.outdooractivityplanner.planning.entity.OutdoorActivity;
import com.github.ingaelsta.outdooractivityplanner.planning.response.OutdoorPlanResponse;
import com.github.ingaelsta.outdooractivityplanner.planning.service.OutdoorPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@Validated
@RequestMapping ("api/v1/outdoor-planner/activity")
public class OutdoorPlanController {

    private final OutdoorPlanService outdoorPlanService;

    @Autowired
    public OutdoorPlanController(OutdoorPlanService outdoorPlanService) {
        this.outdoorPlanService = outdoorPlanService;
    }

    @PostMapping (consumes = MediaType.APPLICATION_JSON_VALUE)
    public OutdoorPlanResponse saveOutdoorPlan (
            @Validated
            @RequestBody
            OutdoorActivity plan) {
        return outdoorPlanService.saveOutdoorPlan(plan);
    }

    @GetMapping(value = "/all")
    public List<OutdoorActivity> getAllPlans() {
        return outdoorPlanService.getAllPlans();
    }

    @DeleteMapping(value = "/{id}")
    public void deleteOutdoorPlanById(@PathVariable (value = "id") @NotNull Long id) {
        outdoorPlanService.deleteOutdoorPlanById(id);
    }

    @PostMapping (value = "/safe", consumes = MediaType.APPLICATION_JSON_VALUE)
    public OutdoorPlanResponse saveSafeOutdoorPlan (
            @Validated
            @RequestBody
            OutdoorActivity plan) {
        return outdoorPlanService.saveSafeOutdoorPlan(plan);
    }

}
