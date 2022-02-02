package com.github.IngaElsta.spring_boot_task.planning;

import java.time.LocalDate;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class SkiPlan {

    private SkiLocation location;
    private LocalDate skiingDate;

}
