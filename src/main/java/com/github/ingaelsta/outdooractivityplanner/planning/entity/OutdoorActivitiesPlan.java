package com.github.ingaelsta.outdooractivityplanner.planning.entity;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class OutdoorActivitiesPlan {

    @NotNull
    private OutdoorActivitiesLocation location;

    @NotNull
    private LocalDate date;

}
