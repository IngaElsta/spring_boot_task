package com.github.IngaElsta.spring_boot_task;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class SkiLocation {

    private String lat;
    private String lon;

}
