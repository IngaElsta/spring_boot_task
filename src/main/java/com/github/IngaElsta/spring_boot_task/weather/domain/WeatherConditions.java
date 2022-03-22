package com.github.IngaElsta.spring_boot_task.weather.domain;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Builder;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
public class WeatherConditions {
    @NotNull
    private LocalDate date;

    @NotEmpty
    private String weatherDescription; //weather: description

    @NotNull
    private Temperature temperature;

    @NotNull
    private Wind wind;

    private List<Alert> alerts;
}