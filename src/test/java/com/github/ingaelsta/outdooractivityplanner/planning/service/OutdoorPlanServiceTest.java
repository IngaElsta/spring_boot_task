package com.github.ingaelsta.outdooractivityplanner.planning.service;

import com.github.ingaelsta.outdooractivityplanner.planning.entity.OutdoorActivity;
import com.github.ingaelsta.outdooractivityplanner.planning.exception.PastDateException;
import com.github.ingaelsta.outdooractivityplanner.planning.repository.OutdoorActivitiesRepository;
import com.github.ingaelsta.outdooractivityplanner.planning.response.OutdoorPlanResponse;
import com.github.ingaelsta.outdooractivityplanner.weather.model.Alert;
import com.github.ingaelsta.outdooractivityplanner.weather.model.Temperature;
import com.github.ingaelsta.outdooractivityplanner.weather.model.WeatherConditions;
import com.github.ingaelsta.outdooractivityplanner.commons.Conversion;
import com.github.ingaelsta.outdooractivityplanner.commons.model.Location;
import com.github.ingaelsta.outdooractivityplanner.weather.model.Wind;
import com.github.ingaelsta.outdooractivityplanner.weather.exception.WeatherDataException;
import com.github.ingaelsta.outdooractivityplanner.weather.service.WeatherDataService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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


    Double latitude = 55.87;
    Double longitude = 26.52;
    private final Location location = new Location(latitude, longitude);
    private final LocalDate date = Conversion.convertDate(1643536800).toLocalDate();

    private Map<LocalDate, WeatherConditions> weatherConditionsMap, weatherConditionsMapWithAlerts;

    private List<Alert> alerts;

    private final Alert alert1 = new Alert("Yellow Flooding Warning",
            date.atStartOfDay().plusHours(3),
            date.atStartOfDay().plusHours(7));
    private final Alert alert2 = new Alert("Red Wind Warning",
            date.atStartOfDay().plusHours(0),
            date.atStartOfDay().plusHours(23).plusMinutes(59).plusSeconds(59));

    @BeforeEach
    public void setup() {
        outdoorServiceMock = new OutdoorPlanService(
                weatherDataServiceMock,
                outdoorPlanRepositoryMock);
        Temperature temperature = new Temperature(1.64, 1.09, -0.16, -0.94);
        Wind wind = new Wind(8.23, 17.56, "S");
        List<String> weatherDescriptions = new ArrayList<>();
        weatherDescriptions.add("rain and snow");

        alerts = new ArrayList<>();
        alerts.add(alert1);
        alerts.add(alert2);

        weatherConditionsMap = new HashMap<>();
        weatherConditionsMap.put(date, new WeatherConditions(
                date, weatherDescriptions, temperature, wind, new ArrayList<>()));

        weatherConditionsMapWithAlerts = new HashMap<>();
        weatherConditionsMapWithAlerts.put(date, new WeatherConditions(
                date, weatherDescriptions, temperature, wind, alerts));
    }

    //getWeather
    @Test
    public void WhenWeatherDataProcessedSuccessfully_GetWeatherShouldReturnData() {

        when(weatherDataServiceMock.retrieveWeather(location)).thenReturn(weatherConditionsMap);

        Map<LocalDate, WeatherConditions> result = outdoorServiceMock.getWeather(location);

        assertEquals(weatherConditionsMap, result);
    }

    @Test
    public void WhenWeatherDataProcessingHasFailed_GetWeatherShouldThrowException() {
        Location location = new Location(55.87, 26.52);

        when(weatherDataServiceMock.retrieveWeather(location))
                .thenThrow(new WeatherDataException("Failed") {
        });

        assertThrows(WeatherDataException.class, () -> outdoorServiceMock.getWeather(location));
    }

    //save activity
    @Test
    public void WhenAttemptingToSaveActivityOnDayWithoutAlerts_ReturnsSavedEntityAndEmptyAlertList() {
        OutdoorActivity outdoorActivity = new OutdoorActivity(latitude, longitude, date);
        outdoorActivity.setId(1L);

        when(outdoorPlanRepositoryMock.save(outdoorActivity)).thenReturn(outdoorActivity);
        when(weatherDataServiceMock.retrieveWeather(location)).thenReturn(weatherConditionsMap);

        OutdoorPlanResponse expected = new OutdoorPlanResponse(outdoorActivity, new ArrayList<>());

        OutdoorPlanResponse result = outdoorServiceMock.saveOutdoorPlan(outdoorActivity);

        assertEquals(expected, result);
    }
    @Test
    public void WhenAttemptingToSaveActivityOnDayWithAlerts_ReturnsSavedEntityAndAlertList() {
        OutdoorActivity outdoorActivity = new OutdoorActivity(latitude, longitude, date);
        outdoorActivity.setId(1L);

        when(outdoorPlanRepositoryMock.save(outdoorActivity)).thenReturn(outdoorActivity);
        when(weatherDataServiceMock.retrieveWeather(location)).thenReturn(weatherConditionsMapWithAlerts);

        OutdoorPlanResponse expected = new OutdoorPlanResponse(outdoorActivity, alerts);

        OutdoorPlanResponse result = outdoorServiceMock.saveOutdoorPlan(outdoorActivity);

        assertEquals(expected, result);
    }

    @Test
    public void WhenAttemptingToSaveActivityOnDayAfterWeatherPrognosisAvailable_ReturnsSavedEntityAndAlertList() {
        LocalDate afterPrognosisPlanDate = date.plusDays(2);
        OutdoorActivity outdoorActivity = new OutdoorActivity(latitude, longitude, afterPrognosisPlanDate);
        outdoorActivity.setId(1L);

        when(outdoorPlanRepositoryMock.save(outdoorActivity)).thenReturn(outdoorActivity);
        when(weatherDataServiceMock.retrieveWeather(location)).thenReturn(weatherConditionsMap);

        Alert alertsUnknownAlert = new Alert(
                (String.format("%s is after the last day when alerts can be predicted", afterPrognosisPlanDate)),
                afterPrognosisPlanDate.atStartOfDay(),
                afterPrognosisPlanDate.atStartOfDay());
        List<Alert> alertsUnknown = new ArrayList<>();
        alertsUnknown.add(alertsUnknownAlert);

        OutdoorPlanResponse expected = new OutdoorPlanResponse(outdoorActivity, alertsUnknown);

        OutdoorPlanResponse result = outdoorServiceMock.saveOutdoorPlan(outdoorActivity);

        assertEquals(expected, result);
    }

    @Test
    public void WhenAttemptingToSaveActivityBeforePrognosisRange_ThrowsPastDateException() {
        LocalDate afterPrognosisPlanDate = date.minusDays(2);
        OutdoorActivity outdoorActivity = new OutdoorActivity(latitude, longitude, afterPrognosisPlanDate);
        outdoorActivity.setId(1L);

        when(outdoorPlanRepositoryMock.save(outdoorActivity)).thenReturn(outdoorActivity);
        when(weatherDataServiceMock.retrieveWeather(location)).thenReturn(weatherConditionsMap);

        assertThrows(PastDateException.class, () -> outdoorServiceMock.saveOutdoorPlan(outdoorActivity));
    }
}
