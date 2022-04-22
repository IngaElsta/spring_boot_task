package com.github.ingaelsta.outdooractivityplanner.planning.repository;

import com.github.ingaelsta.outdooractivityplanner.planning.entity.OutdoorActivity;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;

public interface OutdoorActivitiesRepository
        extends CrudRepository<OutdoorActivity, Long> {
    List<OutdoorActivity> findByPlanDate (LocalDate planDate);

}
