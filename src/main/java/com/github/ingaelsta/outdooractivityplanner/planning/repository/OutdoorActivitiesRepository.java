package com.github.ingaelsta.outdooractivityplanner.planning.repository;

import com.github.ingaelsta.outdooractivityplanner.planning.entity.OutdoorActivitiesDAO;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;

public interface OutdoorActivitiesRepository
        extends CrudRepository<OutdoorActivitiesDAO, Long> {
    List<OutdoorActivitiesDAO> findByPlandate (LocalDate date);

}
