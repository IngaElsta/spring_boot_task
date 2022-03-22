package com.github.IngaElsta.spring_boot_task.weather.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.Map;

@Data
@AllArgsConstructor
public class WeatherConditionsMapWrapper {
    private Map<LocalDate, WeatherConditions> weatherConditionsMap;

    public WeatherConditions put (LocalDate date, WeatherConditions conditions)
            throws UnsupportedOperationException, ClassCastException, IllegalArgumentException {
        weatherConditionsMap.put(date, conditions);
        return conditions;
    }

}
