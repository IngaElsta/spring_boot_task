package com.github.IngaElsta.spring_boot_task;

import lombok.Data;
import lombok.Builder;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class SkiLocation {

    @NotBlank
    private String latitude;

    @NotBlank
    private String longitude;

}
