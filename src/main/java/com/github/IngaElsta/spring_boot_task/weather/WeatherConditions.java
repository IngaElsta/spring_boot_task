package com.github.IngaElsta.spring_boot_task.weather;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;
import lombok.Builder;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class WeatherConditions {
    @NotEmpty
    private LocalDate date;

    @NotEmpty
    private String weather; //weather: description

    @NotNull
    private Temperature temperature;

    @NotNull
    private Wind wind;

    private List<Alert> alerts;
}
