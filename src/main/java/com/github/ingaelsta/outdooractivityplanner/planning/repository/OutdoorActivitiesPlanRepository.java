package com.github.ingaelsta.outdooractivityplanner.planning.repository;

import com.github.ingaelsta.outdooractivityplanner.planning.entity.OutdoorActivitiesPlanDAO;
import org.springframework.data.repository.CrudRepository;

public interface OutdoorActivitiesPlanRepository
        extends CrudRepository<OutdoorActivitiesPlanDAO, Long> {

}
