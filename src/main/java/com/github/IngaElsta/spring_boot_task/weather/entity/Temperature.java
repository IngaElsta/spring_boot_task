package com.github.IngaElsta.spring_boot_task.weather.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class Temperature {
    @NotNull
    private Double morn, day, eve, night; //temp: morn, day, eve, night
}
