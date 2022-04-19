package com.github.ingaelsta.outdooractivityplanner.planning.entity;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;

@Data
@AllArgsConstructor
@Entity(name = "OutdoorActivitiesPlan")
@Table(name = "OUTDOORACTIVITIESPLAN")
public class OutdoorActivitiesPlanDAO {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private Double latitude;
    private Double longitude;
    private LocalDate plandate;

}
