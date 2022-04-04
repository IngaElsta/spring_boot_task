package com.github.IngaElsta.spring_boot_task.planning.domain;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class SkiPlan {

    @NotNull
    private SkiLocation location;

    @NotNull
    private LocalDate skiingDate;

}
