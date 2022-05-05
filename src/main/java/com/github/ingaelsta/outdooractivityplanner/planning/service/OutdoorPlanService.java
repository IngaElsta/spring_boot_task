package com.github.ingaelsta.outdooractivityplanner.planning.service;

import com.github.ingaelsta.outdooractivityplanner.planning.entity.OutdoorActivity;
import com.github.ingaelsta.outdooractivityplanner.commons.model.Location;
import com.github.ingaelsta.outdooractivityplanner.planning.exception.PastDateException;
import com.github.ingaelsta.outdooractivityplanner.planning.repository.OutdoorActivitiesRepository;
import com.github.ingaelsta.outdooractivityplanner.planning.response.OutdoorPlanResponse;
import com.github.ingaelsta.outdooractivityplanner.weather.exception.OWMDataException;
import com.github.ingaelsta.outdooractivityplanner.weather.model.Alert;
import com.github.ingaelsta.outdooractivityplanner.weather.model.WeatherConditions;
import com.github.ingaelsta.outdooractivityplanner.weather.service.WeatherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class OutdoorPlanService {

    //todo: would be better to call the weather controller API instead of service and make those functionalities independent
    private final WeatherService weatherService;
    private final OutdoorActivitiesRepository outdoorActivitiesPlanRepository;

    @Autowired
    public OutdoorPlanService(WeatherService weatherService,
                              OutdoorActivitiesRepository outdoorActivitiesPlanRepository) {
        this.weatherService = weatherService;
        this.outdoorActivitiesPlanRepository = outdoorActivitiesPlanRepository;
    }

    //saves plan regardless of weather conditions
    public OutdoorPlanResponse saveOutdoorPlan (OutdoorActivity plan) {
        Location location = new Location(plan.getLatitude(), plan.getLongitude());
        LocalDate planDate = plan.getPlanDate();
        List<Alert> alerts;
        try {
            alerts = getAlerts(location, planDate);
        } catch (PastDateException e) {
            log.error(String.format("%s is in the past", plan));
            throw e;
        }

        //todo: check if a day has plans already and refuse if so?
        outdoorActivitiesPlanRepository.save(plan);

        return new OutdoorPlanResponse(plan, alerts);
    }

    //saves plan only if no alerts are present otherwise returns a list of alerts
    public OutdoorPlanResponse saveSafeOutdoorPlan (OutdoorActivity plan) {
        Location location = new Location(plan.getLatitude(), plan.getLongitude());
        LocalDate planDate = plan.getPlanDate();
        List<Alert> alerts;
        try {
            alerts = getAlerts(location, planDate);
        } catch (PastDateException e) {
            log.error(String.format("%s is in the past", plan));
            throw e;
        }

        if (!alerts.isEmpty()) {
            //todo: check if a day has plans already and refuse if so?
            return new OutdoorPlanResponse(null, alerts);
        }

        outdoorActivitiesPlanRepository.save(plan);
        return new OutdoorPlanResponse(plan, alerts);
    }

    //retrieves all plans
    public List<OutdoorActivity> getAllPlans() {
        return (List<OutdoorActivity>) outdoorActivitiesPlanRepository.findAll();
    }

    //todo: add a method to retrieve all plans past a certain date
    //todo: add a method to retrieve all future plans that have an alert currently
    //todo: add method to retrieve a plan by id, date or probably location
    //todo: add method to delete plans

    //delete activity plan by passed id
    public void deleteOutdoorPlan(Long id) {
        outdoorActivitiesPlanRepository.deleteById(id);
    }

    private List<Alert> getAlerts(Location location, LocalDate planDate) {
        Map<LocalDate, WeatherConditions> weatherConditionsMap = weatherService.getWeather(location);

        LocalDate weatherConditionFirstDay = weatherConditionsMap.keySet().stream()
                .findFirst()
                .orElse(null);

        if (weatherConditionFirstDay == null) {
            throw new OWMDataException("Failed to retrieve data from weather service");
        }

        if (planDate.isBefore(weatherConditionFirstDay)) {
            //todo: probably should check for past while validating when I figure out testing for it
            throw new PastDateException(String.format("%s is in the past", planDate));
        }

        if (weatherConditionsMap.containsKey(planDate)) {
            return weatherConditionsMap.get(planDate).getAlerts();
        }
        List<Alert> alerts = new ArrayList<>();
        Alert tooFarToGetAlerts = new Alert(
                (String.format("%s is after the last day when alerts can be predicted", planDate)),
                planDate.atStartOfDay(),
                planDate.atStartOfDay());
        alerts.add(tooFarToGetAlerts);
        return alerts;
    }
}
