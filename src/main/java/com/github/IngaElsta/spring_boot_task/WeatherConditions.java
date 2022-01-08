package com.github.IngaElsta.spring_boot_task;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class WeatherConditions {
    private LocalDate date;
    private Temperature temperature;
    private String weather;
    private List<String> alerts;

    class Temperature {
        protected String morning, day, evening;
    }

}
