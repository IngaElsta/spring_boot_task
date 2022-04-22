package com.github.ingaelsta.outdooractivityplanner.planning.service;

import com.github.ingaelsta.outdooractivityplanner.planning.entity.OutdoorActivity;
import com.github.ingaelsta.outdooractivityplanner.commons.model.Location;
import com.github.ingaelsta.outdooractivityplanner.planning.exception.PastDateException;
import com.github.ingaelsta.outdooractivityplanner.planning.repository.OutdoorActivitiesRepository;
import com.github.ingaelsta.outdooractivityplanner.planning.response.OutdoorPlanResponse;
import com.github.ingaelsta.outdooractivityplanner.weather.model.Alert;
import com.github.ingaelsta.outdooractivityplanner.weather.model.WeatherConditions;
import com.github.ingaelsta.outdooractivityplanner.weather.service.WeatherDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class OutdoorPlanService {

    private final WeatherDataService weatherDataService;
    private final OutdoorActivitiesRepository outdoorActivitiesPlanRepository;

    @Autowired
    public OutdoorPlanService(WeatherDataService weatherDataService,
                              OutdoorActivitiesRepository outdoorActivitiesPlanRepository) {
        this.weatherDataService = weatherDataService;
        this.outdoorActivitiesPlanRepository = outdoorActivitiesPlanRepository;
    }

    //return weather information for following 7 days
    public Map<LocalDate, WeatherConditions> getWeather (Location location) {
        //todo: implement actual connecting to cache and processing
        Map<LocalDate, WeatherConditions> weatherConditionsMap = weatherDataService.retrieveWeather(location);
        return weatherConditionsMap;
    }

    //saves plan regardless of weather conditions
    public OutdoorPlanResponse saveOutdoorPlan  (OutdoorActivity plan) {
        Location location = new Location(plan.getLatitude(), plan.getLongitude());
        LocalDate planDate = plan.getPlanDate();
        List<Alert> alerts = getAlerts(location, planDate);

        //todo: check if a day has plans already and refuse if so?
        outdoorActivitiesPlanRepository.save(plan);

        return new OutdoorPlanResponse(plan, alerts);
    }

    //todo: add safe planning method that refuses to book an activity if weather warning expected or info unavailable
    //todo: add method to retrieve all plans
    //todo: add a method to retrieve all future plans
    //todo: add a method to retrieve all future plans that have an alert currently
    //todo: add method to retrieve a plan by id, date or probably location
    //todo: add method to delete plans


    private List<Alert> getAlerts(Location location, LocalDate planDate) {
        Map<LocalDate, WeatherConditions> weatherConditionsMap = weatherDataService.retrieveWeather(location);

        LocalDate weatherConditionFirstDay = weatherConditionsMap.keySet().stream().findFirst().get();
        if (planDate.isBefore(weatherConditionFirstDay)) {
            //todo: probably should check for past while validating when I figure out testing for it
            throw new PastDateException(planDate + " is in the past");
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
