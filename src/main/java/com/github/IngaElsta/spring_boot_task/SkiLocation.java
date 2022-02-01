package com.github.IngaElsta.spring_boot_task;

import lombok.Data;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Data
public class SkiLocation {

    @NotEmpty
    @Digits(integer = 3, fraction = 4)
    @Min(value = -90)
    @Max(value = 90)
    private String latitude;

    @NotEmpty
    @Digits(integer = 2, fraction = 4)
    @Min(value = -180)
    @Max(value = 180)
    private String longitude;

    private SkiLocation(){};

    public SkiLocation (String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
