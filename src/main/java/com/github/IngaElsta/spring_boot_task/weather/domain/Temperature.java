package com.github.IngaElsta.spring_boot_task.weather.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
public class Temperature {
    @NotNull
    @Digits(integer = 2, fraction = 2)
    private Double morn, day, eve, night; //temp: morn, day, eve, night
}
