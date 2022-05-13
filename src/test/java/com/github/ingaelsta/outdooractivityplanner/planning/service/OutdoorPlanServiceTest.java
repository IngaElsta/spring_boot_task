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
import com.github.ingaelsta.outdooractivityplanner.weather.service.WeatherService;
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

    private final WeatherService weatherServiceMock = Mockito.mock(WeatherService.class);
    private final OutdoorActivitiesRepository outdoorPlanRepositoryMock = Mockito.mock(OutdoorActivitiesRepository.class);
    private OutdoorPlanService outdoorServiceMock;


    private final Double latitude = 55.87;
    private final Double longitude = 26.52;
    private final Location location = new Location(latitude, longitude);
    private final LocalDate date = Conversion.convertDate(1643536800).toLocalDate();

    private Map<LocalDate, WeatherConditions> weatherConditionsMap, weatherConditionsMapWithAlerts;

    private List<Alert> alerts;

    @BeforeEach
    public void setup() {
        outdoorServiceMock = new OutdoorPlanService(
                weatherServiceMock,
                outdoorPlanRepositoryMock);

        {
            Temperature temperature = new Temperature(1.64, 1.09, -0.16, -0.94);
            Wind wind = new Wind(8.23, 17.56, "S");
            List<String> weatherDescriptions = new ArrayList<>();
            weatherDescriptions.add("rain and snow");

            Alert alert1 = new Alert("Yellow Flooding Warning",
                    date.atStartOfDay().plusHours(3),
                    date.atStartOfDay().plusHours(7));
            Alert alert2 = new Alert("Red Wind Warning",
                    date.atStartOfDay().plusHours(0),
                    date.atStartOfDay().plusHours(23).plusMinutes(59).plusSeconds(59));

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
    }

    //save activity
    @Test
    public void When_DayWithoutAlerts_Then_saveOutdoorPlanReturnsSavedEntityAndEmptyAlertList() {
        OutdoorActivity outdoorActivity = new OutdoorActivity(latitude, longitude, date);
        outdoorActivity.setId(1L);

        when(outdoorPlanRepositoryMock.save(outdoorActivity))
                .thenReturn(outdoorActivity);
        when(weatherServiceMock.getWeather(location))
                .thenReturn(weatherConditionsMap);

        OutdoorPlanResponse expected = new OutdoorPlanResponse(outdoorActivity, new ArrayList<>());

        OutdoorPlanResponse result = outdoorServiceMock.saveOutdoorPlan(outdoorActivity);

        assertEquals(expected, result);
    }

    @Test
    public void When_DayWithAlerts_Then_saveOutdoorPlanReturnsSavedEntityAndListOfAlerts() {
        OutdoorActivity outdoorActivity = new OutdoorActivity(latitude, longitude, date);
        outdoorActivity.setId(1L);

        when(outdoorPlanRepositoryMock.save(outdoorActivity))
                .thenReturn(outdoorActivity);
        when(weatherServiceMock.getWeather(location))
                .thenReturn(weatherConditionsMapWithAlerts);

        OutdoorPlanResponse expected = new OutdoorPlanResponse(outdoorActivity, alerts);

        OutdoorPlanResponse result = outdoorServiceMock.saveOutdoorPlan(outdoorActivity);

        assertEquals(expected, result);
    }

    @Test
    public void When_PlanDateAfterWeatherPrognosisAvailable_Then_saveOutdoorPlanReturnsSavedEntityAndListWithAlert() {
        LocalDate afterPrognosisPlanDate = date.plusDays(2);
        OutdoorActivity outdoorActivity = new OutdoorActivity(latitude, longitude, afterPrognosisPlanDate);
        outdoorActivity.setId(1L);

        when(outdoorPlanRepositoryMock.save(outdoorActivity))
                .thenReturn(outdoorActivity);
        when(weatherServiceMock.getWeather(location))
                .thenReturn(weatherConditionsMap);

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
    public void When_PlanDateBeforePrognosisRange_Then_saveOutdoorPlanThrowsPastDateException() {
        LocalDate afterPrognosisPlanDate = date.minusDays(2);
        OutdoorActivity outdoorActivity = new OutdoorActivity(latitude, longitude, afterPrognosisPlanDate);
        outdoorActivity.setId(1L);

        when(outdoorPlanRepositoryMock.save(outdoorActivity))
                .thenReturn(outdoorActivity);
        when(weatherServiceMock.getWeather(location))
                .thenReturn(weatherConditionsMap);

        assertThrows(PastDateException.class, () -> outdoorServiceMock.saveOutdoorPlan(outdoorActivity));
    }

    @Test
    public void When_WeatherDataException_Then_saveOutdoorPlanSavedEntityAndListOfAlerts() {
        OutdoorActivity outdoorActivity = new OutdoorActivity(latitude, longitude, date);
        outdoorActivity.setId(1L);

        when(outdoorPlanRepositoryMock.save(outdoorActivity))
                .thenReturn(outdoorActivity);
        when(weatherServiceMock.getWeather(location))
                .thenThrow(new WeatherDataException("Failed") {
                });

        Alert failedToRetrieveWeatherData = new Alert(
                "Failed to retrieve weather data",
                date.atStartOfDay(),
                date.atStartOfDay());
        List<Alert> alertsUnknown = new ArrayList<>();
        alertsUnknown.add(failedToRetrieveWeatherData);

        OutdoorPlanResponse expected = new OutdoorPlanResponse(outdoorActivity, alertsUnknown);

        OutdoorPlanResponse result = outdoorServiceMock.saveOutdoorPlan(outdoorActivity);

        assertEquals(expected, result);
    }

    //get all saved activities
    @Test
    public void When_NonemptyPlanListRetrieved_Then_getAllPlansReturnsData () {
        List<OutdoorActivity> expected = new ArrayList<>();

        OutdoorActivity activity1  = new OutdoorActivity(latitude, longitude, date);
        activity1.setId(1L);
        expected.add(activity1);

        OutdoorActivity activity2  = new OutdoorActivity(latitude, longitude, date.plusDays(2));
        activity1.setId(2L);
        expected.add(activity2);

        when(outdoorPlanRepositoryMock.findAll())
                .thenReturn(expected);

        List<OutdoorActivity> result = outdoorServiceMock.getAllPlans();

        assertEquals(expected, result);
    }

    @Test
    public void When_EmptyPlanListRetrieved_Then_getAllPlansReturnsEmptyList () {
        List<OutdoorActivity> expected = new ArrayList<>();

        when(outdoorPlanRepositoryMock.findAll())
                .thenReturn(expected);

        List<OutdoorActivity> result = outdoorServiceMock.getAllPlans();

        assertEquals(expected, result);
    }

    //delete activity by id
    @Test void When_IdPassed_Then_deleteByIdCallsService() {
        doNothing().when(outdoorPlanRepositoryMock).deleteById(1L);
        outdoorServiceMock.deleteOutdoorPlanById(1L);
        verify(outdoorPlanRepositoryMock).deleteById(1L);
    }

    @Test void When_IdNotPassed_Then_deleteByIdThrowsIllegalArgumentException() {
        doThrow(new IllegalArgumentException()).when(outdoorPlanRepositoryMock).deleteById(null);
        outdoorServiceMock.deleteOutdoorPlanById(1L);
        verify(outdoorPlanRepositoryMock).deleteById(1L);

        assertThrows(IllegalArgumentException.class, () -> outdoorServiceMock.deleteOutdoorPlanById(null));
    }

    //safe save activity
    @Test
    public void When_DayWithoutAlerts_Then_saveSafeOutdoorPlanReturnsSavedEntityAndEmptyAlertList() {
        OutdoorActivity outdoorActivity = new OutdoorActivity(latitude, longitude, date);
        outdoorActivity.setId(1L);

        when(outdoorPlanRepositoryMock.save(outdoorActivity))
                .thenReturn(outdoorActivity);
        when(weatherServiceMock.getWeather(location))
                .thenReturn(weatherConditionsMap);

        OutdoorPlanResponse expected = new OutdoorPlanResponse(outdoorActivity, new ArrayList<>());

        OutdoorPlanResponse result = outdoorServiceMock.saveSafeOutdoorPlan(outdoorActivity);

        assertEquals(expected, result);
    }

    @Test
    public void When_DayWithAlerts_Then_saveSafeOutdoorPlanSkipsSavingAndReturnsOnlyListOfAlerts() {
        OutdoorActivity outdoorActivity = new OutdoorActivity(latitude, longitude, date);
        outdoorActivity.setId(1L);

        when(outdoorPlanRepositoryMock.save(outdoorActivity))
                .thenReturn(outdoorActivity);
        when(weatherServiceMock.getWeather(location))
                .thenReturn(weatherConditionsMapWithAlerts);

        OutdoorPlanResponse expected = new OutdoorPlanResponse(null, alerts);

        OutdoorPlanResponse result = outdoorServiceMock.saveSafeOutdoorPlan(outdoorActivity);

        assertEquals(expected, result);
    }

    @Test
    public void When_PlanDateAfterWeatherPrognosisAvailable_Then_saveSafeOutdoorPlanSkipsSavingAndReturnsOnlyListWithAlert() {
        LocalDate afterPrognosisPlanDate = date.plusDays(2);
        OutdoorActivity outdoorActivity = new OutdoorActivity(latitude, longitude, afterPrognosisPlanDate);
        outdoorActivity.setId(1L);

        when(outdoorPlanRepositoryMock.save(outdoorActivity))
                .thenReturn(outdoorActivity);
        when(weatherServiceMock.getWeather(location))
                .thenReturn(weatherConditionsMap);

        Alert alertsUnknownAlert = new Alert(
                (String.format("%s is after the last day when alerts can be predicted", afterPrognosisPlanDate)),
                afterPrognosisPlanDate.atStartOfDay(),
                afterPrognosisPlanDate.atStartOfDay());
        List<Alert> alertsUnknown = new ArrayList<>();
        alertsUnknown.add(alertsUnknownAlert);

        OutdoorPlanResponse expected = new OutdoorPlanResponse(null, alertsUnknown);

        OutdoorPlanResponse result = outdoorServiceMock.saveSafeOutdoorPlan(outdoorActivity);

        assertEquals(expected, result);
    }

    @Test
    public void When_PlanDateBeforePrognosisRange_Then_saveSafeOutdoorPlanThrowsPastDateException() {
        LocalDate afterPrognosisPlanDate = date.minusDays(2);
        OutdoorActivity outdoorActivity = new OutdoorActivity(latitude, longitude, afterPrognosisPlanDate);
        outdoorActivity.setId(1L);

        when(outdoorPlanRepositoryMock.save(outdoorActivity))
                .thenReturn(outdoorActivity);
        when(weatherServiceMock.getWeather(location))
                .thenReturn(weatherConditionsMap);

        assertThrows(PastDateException.class, () -> outdoorServiceMock.saveSafeOutdoorPlan(outdoorActivity));
    }

    @Test
    public void When_WeatherDataException_Then_saveSafeOutdoorPlanThrowsWeatherDataException() {
        OutdoorActivity outdoorActivity = new OutdoorActivity(latitude, longitude, date);
        outdoorActivity.setId(1L);

        when(outdoorPlanRepositoryMock.save(outdoorActivity))
                .thenReturn(outdoorActivity);
        when(weatherServiceMock.getWeather(location))
                .thenThrow(new WeatherDataException("Failed") {
                });

        Alert failedToRetrieveWeatherData = new Alert(
                "Failed to retrieve weather data",
                date.atStartOfDay(),
                date.atStartOfDay());
        List<Alert> alertsUnknown = new ArrayList<>();
        alertsUnknown.add(failedToRetrieveWeatherData);

        OutdoorPlanResponse expected = new OutdoorPlanResponse(null, alertsUnknown);

        OutdoorPlanResponse result = outdoorServiceMock.saveSafeOutdoorPlan(outdoorActivity);

        assertEquals(expected, result);
    }
}
