package com.github.IngaElsta.spring_boot_task;

import lombok.Builder;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Data
@Builder
public class SkiLocation {

    @NotEmpty(message = "Latitude value should not be empty")
    @Digits(integer = 2, fraction = 4, message = "Latitude should contain a decimal number in format 00.0000")
    @Min(value = -90, message = "Latitude should not be less than -90 (90 South)")
    @Max(value = 90, message = "Latitude should not be more than 90 (90 North)")
    private String latitude;

    @NotEmpty(message = "Longitude value should not be empty")
    @Digits(integer = 3, fraction = 4, message = "Longitude should contain a decimal number in format 000.0000")
    @Min(value = -180, message = "Longitude should not be less than -180 (180 East)")
    @Max(value = 180, message = "Longitude should not be less than 180 (180 West)")
    private String longitude;
}
