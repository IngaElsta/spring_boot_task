package com.github.ingaelsta.outdooractivityplanner.favorites.controller;

import com.github.ingaelsta.outdooractivityplanner.favorites.entity.FavoriteLocation;
import com.github.ingaelsta.outdooractivityplanner.favorites.service.FavoriteLocationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FavoriteLocationControllerUnitTest {

    private final FavoriteLocationService favoriteLocationServiceMock = Mockito.mock(FavoriteLocationService.class);
    private FavoriteLocationController favoriteLocationControllerMock;

    private final FavoriteLocation location1 = new FavoriteLocation(55.87, 26.52, "Daugavpils");
    private final FavoriteLocation location2 = new FavoriteLocation(-63.81,-57.69,"Watching Antarctic birds");


    @BeforeEach
    void setup() {
        favoriteLocationControllerMock = new FavoriteLocationController(favoriteLocationServiceMock);
        location1.setId(1L);
        location2.setId(2L);
    }

    @Test
    void whenSavingFavorite_thenReturnSavedEntity() {
        when(favoriteLocationServiceMock.saveFavorite(location1))
                .thenReturn(location1);

        FavoriteLocation result = favoriteLocationControllerMock.saveFavorite(location1);

        assertEquals(location1, result);
    }

    @Test
    void WhenRetrievingAllSavedFavorites_thenReturnsListOfFavorites() {
        List<FavoriteLocation> favoriteLocations = new ArrayList<>();
        favoriteLocations.add(location1);
        favoriteLocations.add(location2);

        when(favoriteLocationServiceMock.getAllFavorites())
                .thenReturn(favoriteLocations);

        List<FavoriteLocation> result = favoriteLocationControllerMock.getAllFavorites();

        assertEquals(favoriteLocations, result);
    }

    @Test
    public void WhenNoFavoritesSavedAndRetrievingAllSavedFavorites_thenReturnsEmptyList () {
        List<FavoriteLocation> favoriteLocations = new ArrayList<>();

        when(favoriteLocationServiceMock.getAllFavorites())
                .thenReturn(favoriteLocations);

        List<FavoriteLocation> result = favoriteLocationControllerMock.getAllFavorites();

        assertEquals(favoriteLocations, result);
    }

    @Test void WhenAttemptingToDeleteActivityById_thenNoExceptionIsThrown() {
        doNothing().when(favoriteLocationServiceMock).deleteFavoriteById(1L);
        favoriteLocationControllerMock.deleteFavoriteById(1L);
        verify(favoriteLocationServiceMock).deleteFavoriteById(1L);
    }

    @Test void WhenAttemptingToDeleteActivityWithoutPassingId_thenIllegalArgumentExceptionIsThrown() {
        doThrow(new IllegalArgumentException()).when(favoriteLocationServiceMock).deleteFavoriteById(null);
        favoriteLocationControllerMock.deleteFavoriteById(1L);
        verify(favoriteLocationServiceMock).deleteFavoriteById(1L);

        assertThrows(IllegalArgumentException.class, () -> favoriteLocationServiceMock.deleteFavoriteById(null));
    }

}