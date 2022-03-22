package com.github.IngaElsta.spring_boot_task.planning.domain;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Builder;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
public class SkiPlan {

    @NotNull
    private SkiLocation location;

    @NotNull
    private LocalDate skiingDate;

}
