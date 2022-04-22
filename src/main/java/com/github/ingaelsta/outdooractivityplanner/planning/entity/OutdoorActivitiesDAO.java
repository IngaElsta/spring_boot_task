package com.github.ingaelsta.outdooractivityplanner.planning.entity;

import java.time.LocalDate;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@Entity(name = "OutdoorActivitiesPlan")
@Table(name = "OUTDOORACTIVITIESPLAN")
public class OutdoorActivitiesDAO {

    @Id
    @SequenceGenerator(
            name = "outdoorplan_sequence",
            sequenceName = "outdoorplan_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy= GenerationType.AUTO,
            generator = "outdoorplan_sequence"
    )
    private Long id;
    @NotNull(message = "Latitude value should not be empty")
    @Min(value = -90, message = "Latitude should not be less than -90 (90 South)")
    @Max(value = 90, message = "Latitude should not be more than 90 (90 North)")
    private Double latitude;

    @NotNull(message = "Longitude value should not be empty")
    @Min(value = -180, message = "Longitude should not be less than -180 (180 East)")
    @Max(value = 180, message = "Longitude should not be less than 180 (180 West)")
    private Double longitude;
    @NotNull
    private LocalDate plandate;

    public OutdoorActivitiesDAO(Double latitude, Double longitude, LocalDate date) {
        this();
        this.latitude = latitude;
        this.longitude = longitude;
        this.plandate = date;
    }

}
