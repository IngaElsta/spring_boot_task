package com.github.ingaelsta.outdooractivityplanner.planning.service;

import com.github.ingaelsta.outdooractivityplanner.planning.entity.OutdoorActivity;
import com.github.ingaelsta.outdooractivityplanner.planning.repository.OutdoorActivitiesRepository;
import com.github.ingaelsta.outdooractivityplanner.weather.model.Temperature;
import com.github.ingaelsta.outdooractivityplanner.weather.model.WeatherConditions;
import com.github.ingaelsta.outdooractivityplanner.commons.Conversion;
import com.github.ingaelsta.outdooractivityplanner.commons.model.Location;
import com.github.ingaelsta.outdooractivityplanner.weather.model.Wind;
import com.github.ingaelsta.outdooractivityplanner.weather.exception.WeatherDataException;
import com.github.ingaelsta.outdooractivityplanner.weather.service.WeatherDataService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class OutdoorPlanServiceTest {

    private final WeatherDataService weatherDataServiceMock = Mockito.mock(WeatherDataService.class);
    private final OutdoorActivitiesRepository outdoorPlanRepositoryMock = Mockito.mock(OutdoorActivitiesRepository.class);

    private OutdoorPlanService outdoorServiceMock;

    @BeforeEach
    public void setup() {
        outdoorServiceMock =new OutdoorPlanService(
                weatherDataServiceMock,
                outdoorPlanRepositoryMock);
    }

    @Test
    public void WhenWeatherDataProcessedSuccessfully_GetWeatherShouldReturnData() {
        LocalDate date = Conversion.convertDate(1643536800).toLocalDate();
        Temperature temperature = new Temperature(1.64, 1.09, -0.16, -0.94);
        Wind wind = new Wind(8.23, 17.56, "S");
        List<String> weatherDescriptions = new ArrayList<>();
        weatherDescriptions.add("rain and snow");

        Map<LocalDate, WeatherConditions> expected = new HashMap<>();
        expected.put(date, new WeatherConditions(
                date, weatherDescriptions, temperature, wind, new ArrayList<>()));
        Location location = new Location(55.87, 26.52);

        when(weatherDataServiceMock.retrieveWeather(location)).thenReturn(expected);

        Map<LocalDate, WeatherConditions> result = outdoorServiceMock.getWeather(location);

        assertEquals(expected, result);
    }

    @Test
    public void WhenWeatherDataProcessingHasFailed_GetWeatherShouldThrowException() {
        Location location = new Location(55.87, 26.52);

        when(weatherDataServiceMock.retrieveWeather(location))
                .thenThrow(new WeatherDataException("Failed") {
        });

        assertThrows(WeatherDataException.class, () -> outdoorServiceMock.getWeather(location));
    }

    @Test
    public void WhenAttemptingToSaveActivity_ReturnsSavedEntity() {
        LocalDate date = LocalDate.now();
        Double latitude = 52.1;
        Double longitude = -0.78;
        OutdoorActivity outdoorActivity = new OutdoorActivity(latitude, longitude, date);
        outdoorActivity.setId(1L);

        when(outdoorPlanRepositoryMock.save(outdoorActivity)).thenReturn(outdoorActivity);

        OutdoorActivity result = outdoorServiceMock.saveOutdoorPlan(outdoorActivity);

        assertEquals(outdoorActivity, result);
    }

}