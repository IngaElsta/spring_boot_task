package com.github.ingaelsta.outdooractivityplanner.planning.response;

import com.github.ingaelsta.outdooractivityplanner.planning.entity.OutdoorActivity;
import com.github.ingaelsta.outdooractivityplanner.weather.model.Alert;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class OutdoorPlanResponse {
    private OutdoorActivity outdoorActivity;
    private List<Alert> alerts;
}
