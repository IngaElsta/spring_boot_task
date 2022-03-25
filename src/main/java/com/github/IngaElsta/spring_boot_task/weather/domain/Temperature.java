package com.github.IngaElsta.spring_boot_task.weather.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Digits;

@Data
@AllArgsConstructor
public class Temperature {
    @Digits(integer = 2, fraction = 2)
    private Double morn, day, eve, night; //temp: morn, day, eve, night
}
