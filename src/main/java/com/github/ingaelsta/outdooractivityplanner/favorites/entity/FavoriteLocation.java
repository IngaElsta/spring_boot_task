package com.github.ingaelsta.outdooractivityplanner.favorites.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@Entity(name = "favorites")
@Table(name = "FAVORITES")
public class FavoriteLocation {
    @Id
    @SequenceGenerator(
            name = "favorites_sequence",
            sequenceName = "favorites_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy= GenerationType.AUTO,
            generator = "favorites_sequence"
    )
    private Long id;
    @NotNull(message = "Latitude value should not be empty")
    @Min(value = -90, message = "Latitude must be greater than or equal to -90 (90 South)")
    @Max(value = 90, message = "Latitude must be less than or equal to 90 (90 North)")
    private Double latitude;

    @NotNull(message = "Longitude value should not be empty")
    @Min(value = -180, message = "Longitude must be greater than or equal to -180 (180 East)")
    @Max(value = 180, message = "Longitude must be less than or equal to 180 (180 West)")
    private Double longitude;

    public FavoriteLocation(Double latitude, Double longitude) {
        this();
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
