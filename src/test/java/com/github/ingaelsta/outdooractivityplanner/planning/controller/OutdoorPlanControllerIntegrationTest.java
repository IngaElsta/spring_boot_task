package com.github.ingaelsta.outdooractivityplanner.planning.controller;

import com.github.ingaelsta.outdooractivityplanner.commons.Conversion;
import com.github.ingaelsta.outdooractivityplanner.planning.entity.OutdoorActivity;
import com.github.ingaelsta.outdooractivityplanner.planning.exception.PastDateException;
import com.github.ingaelsta.outdooractivityplanner.planning.response.OutdoorPlanResponse;
import com.github.ingaelsta.outdooractivityplanner.planning.service.OutdoorPlanService;
import com.github.ingaelsta.outdooractivityplanner.weather.model.Alert;

import com.github.ingaelsta.outdooractivityplanner.weather.exception.WeatherDataException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@WebMvcTest(OutdoorPlanController.class)
class OutdoorPlanControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OutdoorPlanService outdoorPlanServiceMock;

    private static final String URL = "/api/v1/outdoor-planner/activity";
    private static final Double latitude = 55.87;
    private static final Double longitude = 26.52;
    private static final LocalDate date = Conversion.convertDate(1643536800).toLocalDate();
    private static List<Alert> alerts;

    @BeforeAll
    public static void setup () {
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

    //post activity
    @Test
    public void WhenSavingValidPlanOnDayWithAlerts_thenReturnsEntityAndListOfAlerts()  throws Exception{

        String requestBody =
                (String.format("{\"latitude\": %s,\"longitude\": %s,\"planDate\":\"%s\"}",
                        latitude, longitude, date.toString()));

        OutdoorActivity outdoorActivity = new OutdoorActivity(latitude, longitude, date);
        OutdoorPlanResponse expected = new OutdoorPlanResponse(outdoorActivity, alerts);

        when(outdoorPlanServiceMock.saveOutdoorPlan(outdoorActivity))
                .thenReturn(expected);

        this.mockMvc
                .perform(post(URL)
                        .content(requestBody)
                        .header("Content-Type", "application/json"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Yellow Flooding Warning")));
    }

    @Test
    public void WhenSavingValidPlanOnDayWithoutAlerts_thenReturnsEntityAndEmptyAlertList()  throws Exception{

        String requestBody =
                (String.format("{\"latitude\": %s,\"longitude\": %s,\"planDate\":\"%s\"}",
                        latitude, longitude, date.toString()));

        OutdoorActivity outdoorActivity = new OutdoorActivity(latitude, longitude, date);
        OutdoorPlanResponse expected = new OutdoorPlanResponse(outdoorActivity, null);

        when(outdoorPlanServiceMock.saveOutdoorPlan(outdoorActivity))
                .thenReturn(expected);

        this.mockMvc
                .perform(post(URL)
                        .content(requestBody)
                        .header("Content-Type", "application/json"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("planDate")));
    }

    @Test
    public void WhenSavingPlanOnDayBeforePrognosisRange_thenThrowsPastDateException()  throws Exception{

        String requestBody =
                (String.format("{\"latitude\": %s,\"longitude\": %s,\"planDate\":\"%s\"}",
                        latitude, longitude, date.toString()));

        OutdoorActivity outdoorActivity = new OutdoorActivity(latitude, longitude, date);

        when(outdoorPlanServiceMock.saveOutdoorPlan(outdoorActivity))
                .thenThrow(new PastDateException("placeholder"));

        this.mockMvc
                .perform(post(URL)
                        .content(requestBody)
                        .header("Content-Type", "application/json"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("placeholder")));
    }

    @Test
    public void WhenSavingPlanWithInvalidCoordinates_thenThrowsMethodArgumentNotValidException()  throws Exception{

        String requestBody =
                (String.format("{\"latitude\": %s,\"longitude\": %s,\"planDate\":\"%s\"}",
                        latitude, "555", date.toString()));

        this.mockMvc
                .perform(post(URL)
                        .content(requestBody)
                        .header("Content-Type", "application/json"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Longitude must be less")));
    }

    @Test
    public void WhenSavingPlanWithArgumentMissing_thenThrowsMethodArgumentNotValidException()  throws Exception{

        String requestBody =
                (String.format("{\"latitude\": %s,\"planDate\":\"%s\"}",
                        latitude, date.toString()));

        this.mockMvc
                .perform(post(URL)
                        .content(requestBody)
                        .header("Content-Type", "application/json"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Longitude value should not be empty")));
    }

    @Test
    public void WhenWeatherDataRetrievalUnsuccessfulWhileAttemptingToSave_thenReturnError() throws Exception {
        String requestBody =
                (String.format("{\"latitude\": %s,\"longitude\": %s,\"planDate\":\"%s\"}",
                        latitude, longitude, date.toString()));

        OutdoorActivity outdoorActivity = new OutdoorActivity(latitude, longitude, date);

        when(outdoorPlanServiceMock.saveOutdoorPlan(outdoorActivity))
                .thenThrow(new WeatherDataException("placeholder") {});

        this.mockMvc
                .perform(post(URL)
                        .content(requestBody)
                        .header("Content-Type", "application/json"))
                .andDo(print())
                .andExpect(status().is5xxServerError())
                .andExpect(content().string(containsString("placeholder")));
    }

    //get all activities
    @Test
    public void WhenRetrievingSavedAllPlans_thenReturnsData () throws Exception{
        List<OutdoorActivity> expected = new ArrayList<>();

        OutdoorActivity activity1  = new OutdoorActivity(latitude, longitude, date);
        activity1.setId(1L);
        expected.add(activity1);

        OutdoorActivity activity2  = new OutdoorActivity(latitude, longitude, date.plusDays(2));
        activity1.setId(2L);
        expected.add(activity2);

        when(outdoorPlanServiceMock.getAllPlans())
                .thenReturn(expected);

        this.mockMvc
                .perform(get((String.format("%s//all", URL))))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(latitude.toString())));
    }

    @Test
    public void WhenNoActivitiesSavedAndRetrievingSavedAllPlans_thenReturnsEmptyList () throws Exception {
        List<OutdoorActivity> expected = new ArrayList<>();

        when(outdoorPlanServiceMock.getAllPlans())
                .thenReturn(expected);
        System.out.println(expected);

        this.mockMvc
                .perform(get((String.format("%s//all", URL))))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("[]")));
    }

    //delete activity
    @Test
    public void WhenDeletingOutdoorPlanById_thenCallsOutdoorPlanService() throws Exception{

        doNothing().when(outdoorPlanServiceMock).deleteOutdoorPlan(1L);

        this.mockMvc
                .perform(delete((String.format("%s?id=1", URL))))
                .andDo(print())
                .andExpect(status().isOk());

        verify(outdoorPlanServiceMock).deleteOutdoorPlan(1L);
    }

    @Test
    public void WhenDeletingByIdWithoutPassingId_thenReturnError() throws Exception {
        this.mockMvc
                .perform(delete((URL)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("is not present")));
    }

    //post safe activity
    @Test
    public void WhenSafeSavingPlanOnDayWithAlerts_thenReturnsOnlyListOfAlerts()  throws Exception{

        String requestBody =
                (String.format("{\"latitude\": %s,\"longitude\": %s,\"planDate\":\"%s\"}",
                        latitude, longitude, date.toString()));

        OutdoorActivity outdoorActivity = new OutdoorActivity(latitude, longitude, date);
        OutdoorPlanResponse expected = new OutdoorPlanResponse(null, alerts);

        when(outdoorPlanServiceMock.saveSafeOutdoorPlan(outdoorActivity))
                .thenReturn(expected);

        this.mockMvc
                .perform(post(String.format("%s//safe", URL))
                        .content(requestBody)
                        .header("Content-Type", "application/json"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Yellow Flooding Warning")))
                .andExpect(content().string(not(containsString("planDate"))));
    }

    @Test
    public void WhenSavingSafeValidPlanOnDayWithoutAlerts_thenReturnEntityAndEmptyAlertList()  throws Exception{

        String requestBody =
                (String.format("{\"latitude\": %s,\"longitude\": %s,\"planDate\":\"%s\"}",
                        latitude, longitude, date.toString()));

        OutdoorActivity outdoorActivity = new OutdoorActivity(latitude, longitude, date);
        OutdoorPlanResponse expected = new OutdoorPlanResponse(outdoorActivity, null);

        when(outdoorPlanServiceMock.saveSafeOutdoorPlan(outdoorActivity))
                .thenReturn(expected);

        this.mockMvc
                .perform(post(String.format("%s//safe", URL))
                        .content(requestBody)
                        .header("Content-Type", "application/json"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("planDate")));
    }

    @Test
    public void WhenSafeSavingPlanOnDayBeforePrognosisRange_thenThrowsPastDateException()  throws Exception{

        String requestBody =
                (String.format("{\"latitude\": %s,\"longitude\": %s,\"planDate\":\"%s\"}",
                        latitude, longitude, date.toString()));

        OutdoorActivity outdoorActivity = new OutdoorActivity(latitude, longitude, date);

        when(outdoorPlanServiceMock.saveSafeOutdoorPlan(outdoorActivity))
                .thenThrow(new PastDateException("placeholder"));

        this.mockMvc
                .perform(post(String.format("%s//safe", URL))
                        .content(requestBody)
                        .header("Content-Type", "application/json"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("placeholder")));
    }

    @Test
    public void WhenSafeSavingPlanWithInvalidCoordinates_thenThrowsMethodArgumentNotValidException()  throws Exception{

        String requestBody =
                (String.format("{\"latitude\": %s,\"longitude\": %s,\"planDate\":\"%s\"}",
                        latitude, "555", date.toString()));

        this.mockMvc
                .perform(post(String.format("%s//safe", URL))
                        .content(requestBody)
                        .header("Content-Type", "application/json"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Longitude must be less")));
    }

    @Test
    public void WhenSafeSavingPlanWithArgumentMissing_thenThrowsMethodArgumentNotValidException()  throws Exception{

        String requestBody =
                (String.format("{\"latitude\": %s,\"planDate\":\"%s\"}",
                        latitude, date.toString()));

        this.mockMvc
                .perform(post(String.format("%s//safe", URL))
                        .content(requestBody)
                        .header("Content-Type", "application/json"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Longitude value should not be empty")));
    }

    @Test
    public void WhenWeatherDataRetrievalUnsuccessfulWhileAttemptingToSafeSave_thenReturnError() throws Exception {
        String requestBody =
                (String.format("{\"latitude\": %s,\"longitude\": %s,\"planDate\":\"%s\"}",
                        latitude, longitude, date.toString()));

        OutdoorActivity outdoorActivity = new OutdoorActivity(latitude, longitude, date);

        when(outdoorPlanServiceMock.saveSafeOutdoorPlan(outdoorActivity))
                .thenThrow(new WeatherDataException("placeholder") {});

        this.mockMvc
                .perform(post(String.format("%s//safe", URL))
                        .content(requestBody)
                        .header("Content-Type", "application/json"))
                .andDo(print())
                .andExpect(status().is5xxServerError())
                .andExpect(content().string(containsString("placeholder")));
    }

}
