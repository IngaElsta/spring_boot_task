package com.github.IngaElsta.spring_boot_task.planning.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.*;

@Data
@AllArgsConstructor
public class SkiLocation {

    @NotNull(message = "Latitude value should not be empty")
    @Digits(integer = 2, fraction = 4, message = "Latitude should contain a decimal number in format 00.0000")
    @Min(value = -90, message = "Latitude should not be less than -90 (90 South)")
    @Max(value = 90, message = "Latitude should not be more than 90 (90 North)")
    private Double latitude;

    @NotNull(message = "Longitude value should not be empty")
    @Digits(integer = 3, fraction = 4, message = "Longitude should contain a decimal number in format 000.0000")
    @Min(value = -180, message = "Longitude should not be less than -180 (180 East)")
    @Max(value = 180, message = "Longitude should not be less than 180 (180 West)")
    private Double longitude;
}
