package com.github.ingaelsta.outdooractivityplanner.planning.service;

import com.github.ingaelsta.outdooractivityplanner.planning.entity.OutdoorActivitiesDAO;
import com.github.ingaelsta.outdooractivityplanner.commons.model.Location;
import com.github.ingaelsta.outdooractivityplanner.planning.repository.OutdoorActivitiesRepository;
import com.github.ingaelsta.outdooractivityplanner.weather.model.WeatherConditions;
import com.github.ingaelsta.outdooractivityplanner.weather.service.WeatherDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;

@Service
public class OutdoorPlanService {

    private final WeatherDataService weatherDataService;
    private final OutdoorActivitiesRepository outdoorActivitiesPlanRepository;;

    @Autowired
    public OutdoorPlanService(WeatherDataService weatherDataService,
                              OutdoorActivitiesRepository outdoorActivitiesPlanRepository) {
        this.weatherDataService = weatherDataService;
        this.outdoorActivitiesPlanRepository = outdoorActivitiesPlanRepository;
    }

    //return weather information for following 7 days
    public Map<LocalDate, WeatherConditions> getWeather (Location location) {
        //TODO: implement actual connecting to cache and processing
        Map<LocalDate, WeatherConditions> weatherConditionsMap = weatherDataService.retrieveWeather(location);
        return weatherConditionsMap;
    }

    public OutdoorActivitiesDAO saveOutdoorPlan  (OutdoorActivitiesDAO plan) {
        return outdoorActivitiesPlanRepository.save(plan);
    }

}
