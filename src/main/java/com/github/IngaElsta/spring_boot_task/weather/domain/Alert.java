package com.github.IngaElsta.spring_boot_task.weather.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class Alert {
    @NotEmpty
    private String alertType; //alerts: event.

    //TODO: add validation that the start of an alert is before its end
    //Only the timing of transitions between alert/no alert are returned
    //alerts: start, end
    private LocalDateTime alertStart, alertEnd;
}
