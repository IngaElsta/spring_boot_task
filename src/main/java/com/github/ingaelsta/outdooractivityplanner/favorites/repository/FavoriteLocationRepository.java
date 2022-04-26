package com.github.ingaelsta.outdooractivityplanner.favorites.repository;

import com.github.ingaelsta.outdooractivityplanner.favorites.entity.FavoriteLocation;
import org.springframework.data.repository.CrudRepository;

public interface FavoriteLocationRepository extends CrudRepository<FavoriteLocation, Long> {
}
