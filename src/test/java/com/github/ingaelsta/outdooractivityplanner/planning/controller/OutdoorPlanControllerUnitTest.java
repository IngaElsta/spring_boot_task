package com.github.ingaelsta.outdooractivityplanner.planning.controller;

import com.github.ingaelsta.outdooractivityplanner.commons.Conversion;
import com.github.ingaelsta.outdooractivityplanner.commons.model.Location;
import com.github.ingaelsta.outdooractivityplanner.planning.entity.OutdoorActivity;
import com.github.ingaelsta.outdooractivityplanner.planning.exception.PastDateException;
import com.github.ingaelsta.outdooractivityplanner.planning.response.OutdoorPlanResponse;
import com.github.ingaelsta.outdooractivityplanner.planning.service.OutdoorPlanService;
import com.github.ingaelsta.outdooractivityplanner.weather.model.Alert;
import com.github.ingaelsta.outdooractivityplanner.weather.model.Temperature;
import com.github.ingaelsta.outdooractivityplanner.weather.model.WeatherConditions;
import com.github.ingaelsta.outdooractivityplanner.weather.model.Wind;

import com.github.ingaelsta.outdooractivityplanner.weather.exception.WeatherDataException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class OutdoorPlanControllerUnitTest {
    
    private final OutdoorPlanService outdoorPlanServiceMock = Mockito.mock(OutdoorPlanService .class);
    
    private OutdoorPlanController outdoorPlanControllerMock;

    private final Double latitude = 55.87;
    private final Double longitude = 26.52;
    private final Location location = new Location(latitude, longitude);
    private final LocalDate date = Conversion.convertDate(1643536800).toLocalDate();
    private List<Alert> alerts;

    @BeforeEach
    public void setup () {
        outdoorPlanControllerMock = new OutdoorPlanController(outdoorPlanServiceMock) ;
        Alert alert1 = new Alert("Yellow Flooding Warning",
                date.atStartOfDay().plusHours(3),
                date.atStartOfDay().plusHours(7));
        Alert alert2 = new Alert("Red Wind Warning",
                date.atStartOfDay().plusHours(0),
                date.atStartOfDay().plusHours(23).plusMinutes(59).plusSeconds(59));
        alerts = new ArrayList<>();
        alerts.add(alert1);
        alerts.add(alert2);
    }

    @Test
    public void WhenNoParametersPassedToGetWeather_ShouldUseDefaultValuesAndReturnData() {
        Temperature temperature = new Temperature(1.64, 1.09, -0.16, -0.94);
        Wind wind = new Wind(8.23, 17.56, "S");
        List<String> weatherDescriptions = new ArrayList<>();
        weatherDescriptions.add("rain and snow");

        Map<LocalDate, WeatherConditions> expected = new HashMap<>();
        expected.put(date, new WeatherConditions(
                date, weatherDescriptions, temperature, wind, new ArrayList<>()));

        when(outdoorPlanServiceMock.getWeather(location))
                .thenReturn(expected);

        Map<LocalDate, WeatherConditions> result = outdoorPlanControllerMock.getWeather(latitude, longitude);

        assertEquals(result, expected);
    }

    @Test
    public void WhenValidLocationPassedToGetWeather_ShouldUsePassedValuesAndReturnData() {
        Temperature temperature = new Temperature(1.64, 1.09, -0.16, -0.94);
        Wind wind = new Wind(8.23, 17.56, "S");
        List<String> weatherDescriptions = new ArrayList<>();
        weatherDescriptions.add("rain and snow");

        Map<LocalDate, WeatherConditions> expected = new HashMap<>();
        expected.put(date, new WeatherConditions(
                date, weatherDescriptions, temperature, wind, new ArrayList<>()));

        when(outdoorPlanServiceMock.getWeather(location))
                .thenReturn(expected);

        Map<LocalDate, WeatherConditions> result = outdoorPlanControllerMock.getWeather(latitude, longitude);

        assertEquals(expected, result);
    }

    @Test
    public void WhenWeatherDataRetrievalUnsuccessful_ShouldReturnError() {
        when(outdoorPlanServiceMock.getWeather(new Location(55.87, 26.52)))
                .thenThrow(new WeatherDataException("placeholder") {});

        assertThrows(WeatherDataException.class, () -> outdoorPlanControllerMock.getWeather(55.87, 26.52));
    }

    @Test
    public void WhenSavingValidPlanOnDayWithAlerts_ShouldReturnEntityAndListOfAlerts() {

        OutdoorActivity outdoorActivity = new OutdoorActivity(latitude, longitude, date);
        OutdoorPlanResponse expected = new OutdoorPlanResponse(outdoorActivity, alerts);

        when(outdoorPlanServiceMock.saveOutdoorPlan(outdoorActivity))
                .thenReturn(expected);

        OutdoorPlanResponse result = outdoorPlanControllerMock.saveOutdoorPlan(outdoorActivity);

        assertEquals(expected, result);
    }

    @Test
    public void WhenSavingValidPlanOnDayWithoutAlerts_ShouldReturnEntityAndEmptyAlertList() {

        OutdoorActivity outdoorActivity = new OutdoorActivity(latitude, longitude, date);
        OutdoorPlanResponse expected = new OutdoorPlanResponse(outdoorActivity, null);

        when(outdoorPlanServiceMock.saveOutdoorPlan(outdoorActivity))
                .thenReturn(expected);

        OutdoorPlanResponse result = outdoorPlanControllerMock.saveOutdoorPlan(outdoorActivity);

        assertEquals(expected, result);
    }

    @Test
    public void WhenSavingPlanOnDayBeforePrognosisRange_ShouldThrowPastDateException() {

        OutdoorActivity outdoorActivity = new OutdoorActivity(latitude, longitude, date);

        when(outdoorPlanServiceMock.saveOutdoorPlan(outdoorActivity))
                .thenThrow(new PastDateException("placeholder"));

        assertThrows(PastDateException.class, () -> outdoorPlanControllerMock.saveOutdoorPlan(outdoorActivity));
    }

}