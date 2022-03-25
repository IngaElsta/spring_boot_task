package com.github.IngaElsta.spring_boot_task.weather.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class Alert {
    @NotEmpty
    private String alertType; //alerts: event.

    @NotNull
    private LocalDateTime alertStart, alertEnd; //alerts: start, end
}
