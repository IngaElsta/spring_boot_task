package com.github.IngaElsta.spring_boot_task.weather.domain;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
    private List<String> weatherDescription; //weather: description

    @NotNull
    private Temperature temperature;

    @NotNull
    private Wind wind;

    private List<Alert> alerts;

    public static LocalDateTime convertDate(long date_seconds){
        Instant instant = Instant.ofEpochSecond(date_seconds);
        return instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
}
