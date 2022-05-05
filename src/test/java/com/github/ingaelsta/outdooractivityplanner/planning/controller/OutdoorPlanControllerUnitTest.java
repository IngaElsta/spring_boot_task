package com.github.ingaelsta.outdooractivityplanner.planning.controller;

import com.github.ingaelsta.outdooractivityplanner.commons.Conversion;
import com.github.ingaelsta.outdooractivityplanner.planning.entity.OutdoorActivity;
import com.github.ingaelsta.outdooractivityplanner.planning.exception.PastDateException;
import com.github.ingaelsta.outdooractivityplanner.planning.response.OutdoorPlanResponse;
import com.github.ingaelsta.outdooractivityplanner.planning.service.OutdoorPlanService;
import com.github.ingaelsta.outdooractivityplanner.weather.model.Alert;

import com.github.ingaelsta.outdooractivityplanner.weather.exception.WeatherDataException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

class OutdoorPlanControllerUnitTest {
    
    private final OutdoorPlanService outdoorPlanServiceMock = Mockito.mock(OutdoorPlanService .class);
    
    private OutdoorPlanController outdoorPlanControllerMock;

    private final Double latitude = 55.87;
    private final Double longitude = 26.52;
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

    //saveOutdoorPlan
    @Test
    public void WhenSavingValidPlanOnDayWithAlerts_thenReturnsEntityAndListOfAlerts() {

        OutdoorActivity outdoorActivity = new OutdoorActivity(latitude, longitude, date);
        OutdoorPlanResponse expected = new OutdoorPlanResponse(outdoorActivity, alerts);

        when(outdoorPlanServiceMock.saveOutdoorPlan(outdoorActivity))
                .thenReturn(expected);

        OutdoorPlanResponse result = outdoorPlanControllerMock.saveOutdoorPlan(outdoorActivity);

        assertEquals(expected, result);
    }

    @Test
    public void WhenSavingValidPlanOnDayWithoutAlerts_thenReturnsEntityAndEmptyAlertList() {

        OutdoorActivity outdoorActivity = new OutdoorActivity(latitude, longitude, date);
        OutdoorPlanResponse expected = new OutdoorPlanResponse(outdoorActivity, null);

        when(outdoorPlanServiceMock.saveOutdoorPlan(outdoorActivity))
                .thenReturn(expected);

        OutdoorPlanResponse result = outdoorPlanControllerMock.saveOutdoorPlan(outdoorActivity);

        assertEquals(expected, result);
    }

    @Test
    public void WhenSavingPlanOnDayBeforePrognosisRange_thenThrowsPastDateException() {
        OutdoorActivity outdoorActivity = new OutdoorActivity(latitude, longitude, date);

        when(outdoorPlanServiceMock.saveOutdoorPlan(outdoorActivity))
                .thenThrow(new PastDateException("placeholder"));

        assertThrows(PastDateException.class, () -> outdoorPlanControllerMock.saveOutdoorPlan(outdoorActivity));
    }

    @Test
    public void WhenWeatherDataRetrievalUnsuccessfulWhileSavingPlan_thenReturnsWeatherDataException() {
        OutdoorActivity outdoorActivity = new OutdoorActivity(latitude, longitude, date);
        when(outdoorPlanServiceMock.saveOutdoorPlan(outdoorActivity))
                .thenThrow(new WeatherDataException("placeholder") {});

        assertThrows(WeatherDataException.class, () -> outdoorPlanControllerMock.saveOutdoorPlan(outdoorActivity));
    }

    //getAllPlans
    @Test
    public void WhenRetrievingSavedAllPlans_thenReturnsData () {
        List<OutdoorActivity> expected = new ArrayList<>();

        OutdoorActivity activity1  = new OutdoorActivity(latitude, longitude, date);
        activity1.setId(1L);
        expected.add(activity1);

        OutdoorActivity activity2  = new OutdoorActivity(latitude, longitude, date.plusDays(2));
        activity1.setId(2L);
        expected.add(activity2);

        when(outdoorPlanServiceMock.getAllPlans())
                .thenReturn(expected);

        List<OutdoorActivity> result = outdoorPlanControllerMock.getAllPlans();

        assertEquals(expected, result);
    }

    @Test
    public void WhenNoActivitiesSavedAndRetrievingSavedAllPlans_thenReturnsEmptyList () {
        List<OutdoorActivity> expected = new ArrayList<>();

        when(outdoorPlanServiceMock.getAllPlans())
                .thenReturn(expected);

        List<OutdoorActivity> result = outdoorPlanControllerMock.getAllPlans();

        assertEquals(expected, result);
    }

    //deleteOutdoorPlan
    @Test
    public void WhenPassingIdAndDeletingPlan_thenNoExceptionThrown (){
        doNothing().when(outdoorPlanServiceMock).deleteOutdoorPlanById(1L);
        outdoorPlanControllerMock.deleteOutdoorPlanById(1L);
        verify(outdoorPlanServiceMock).deleteOutdoorPlanById(1L);
    }

    @Test
    void WhenAttemptingToDeleteActivityWithoutPassingId_thenIllegalArgumentExceptionIsThrown() {
        doThrow(new IllegalArgumentException()).when(outdoorPlanServiceMock).deleteOutdoorPlanById(null);
        outdoorPlanControllerMock.deleteOutdoorPlanById(1L);
        verify(outdoorPlanServiceMock).deleteOutdoorPlanById(1L);

        assertThrows(IllegalArgumentException.class, () -> outdoorPlanControllerMock.deleteOutdoorPlanById(null));
    }

    //saveSafeOutdoorPlan
    @Test
    public void WhenSafeSavingPlanOnDayWithAlerts_thenReturnsOnlyListOfAlerts() {

        OutdoorActivity outdoorActivity = new OutdoorActivity(latitude, longitude, date);
        OutdoorPlanResponse expected = new OutdoorPlanResponse(null, alerts);

        when(outdoorPlanServiceMock.saveSafeOutdoorPlan(outdoorActivity))
                .thenReturn(expected);

        OutdoorPlanResponse result = outdoorPlanControllerMock.saveSafeOutdoorPlan(outdoorActivity);

        assertEquals(expected, result);
    }

    @Test
    public void WhenSafeSavingValidPlanOnDayWithoutAlerts_thenReturnsEntityAndEmptyAlertList() {

        OutdoorActivity outdoorActivity = new OutdoorActivity(latitude, longitude, date);
        OutdoorPlanResponse expected = new OutdoorPlanResponse(outdoorActivity, null);

        when(outdoorPlanServiceMock.saveSafeOutdoorPlan(outdoorActivity))
                .thenReturn(expected);

        OutdoorPlanResponse result = outdoorPlanControllerMock.saveSafeOutdoorPlan(outdoorActivity);

        assertEquals(expected, result);
    }

    @Test
    public void WhenSafeSavingPlanOnDayBeforePrognosisRange_thenThrowsPastDateException() {

        OutdoorActivity outdoorActivity = new OutdoorActivity(latitude, longitude, date);

        when(outdoorPlanServiceMock.saveSafeOutdoorPlan(outdoorActivity))
                .thenThrow(new PastDateException("placeholder"));

        assertThrows(PastDateException.class, () -> outdoorPlanControllerMock.saveSafeOutdoorPlan(outdoorActivity));
    }

}
