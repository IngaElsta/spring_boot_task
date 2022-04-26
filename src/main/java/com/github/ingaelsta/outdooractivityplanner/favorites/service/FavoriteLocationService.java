package com.github.ingaelsta.outdooractivityplanner.favorites.service;

import com.github.ingaelsta.outdooractivityplanner.favorites.entity.FavoriteLocation;
import com.github.ingaelsta.outdooractivityplanner.favorites.repository.FavoriteLocationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteLocationService {

    private FavoriteLocationRepository favoriteLocationRepository;

    public FavoriteLocationService (FavoriteLocationRepository favoriteLocationRepository) {
        this.favoriteLocationRepository = favoriteLocationRepository;
    }

    public FavoriteLocation saveFavorite (FavoriteLocation location) {
        return favoriteLocationRepository.save(location);
    }

    public List<FavoriteLocation> getAllFavorites() {
        return (List<FavoriteLocation>) favoriteLocationRepository.findAll();
    }

    public void deleteFavoriteById(Long id) {
        favoriteLocationRepository.deleteById(id);
    }
}
