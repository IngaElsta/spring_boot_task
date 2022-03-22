package com.github.IngaElsta.spring_boot_task.weather;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;

@Data
@Builder
public class Temperature {
    @NotEmpty
    @Digits(integer = 2, fraction = 2)
    private String morn, day, eve, night; //temp: morn, day, eve, night
}
