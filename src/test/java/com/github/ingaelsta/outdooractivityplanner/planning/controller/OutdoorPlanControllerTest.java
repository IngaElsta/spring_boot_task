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
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebMvcTest(OutdoorPlanController.class)
class OutdoorPlanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OutdoorPlanService outdoorPlanServiceMock;

    private static final String URL = "/api/v1/outdoor-planner";
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

    @Test
    public void WhenNoParametersPassedToGetWeather_ShouldUseDefaultValuesAndReturnData() throws Exception {
        Temperature temperature = new Temperature(1.64, 1.09, -0.16, -0.94);
        Wind wind = new Wind(8.23, 17.56, "S");
        List<String> weatherDescriptions = new ArrayList<>();
        weatherDescriptions.add("rain and snow");

        Map<LocalDate, WeatherConditions> expected = new HashMap<>();
        expected.put(date, new WeatherConditions(
                date, weatherDescriptions, temperature, wind, new ArrayList<>()));

        when(outdoorPlanServiceMock.getWeather(new Location(56.95, 24.11)))
                .thenReturn(expected);

        this.mockMvc
                .perform(get((String.format("%s//weather", URL))))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("rain and snow")));
    }

    @Test
    public void WhenValidLocationPassedToGetWeather_ShouldUsePassedValuesAndReturnData() throws Exception {
        Temperature temperature = new Temperature(1.64, 1.09, -0.16, -0.94);
        Wind wind = new Wind(8.23, 17.56, "S");
        List<String> weatherDescriptions = new ArrayList<>();
        weatherDescriptions.add("rain and snow");

        Map<LocalDate, WeatherConditions> expected = new HashMap<>();
        expected.put(date, new WeatherConditions(
                date, weatherDescriptions, temperature, wind, new ArrayList<>()));

        when(outdoorPlanServiceMock.getWeather(new Location(55.87, 26.52)))
                .thenReturn(expected);

        this.mockMvc
                .perform(get((String.format("%s//weather?lat=55.87&lon=26.52", URL))))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("rain and snow")));
    }


    @Test
    public void WhenInvalidLocationPassedToGetWeather_ShouldReturnError() throws Exception {
        this.mockMvc
                .perform(get((String.format("%s//weather?lat=555&lon=26.52", URL))))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void WhenWeatherDataRetrievalUnsuccessful_ShouldReturnError() throws Exception {
        when(outdoorPlanServiceMock.getWeather(new Location(55.87, 26.52)))
                .thenThrow(new WeatherDataException("placeholder") {});

        this.mockMvc
                .perform(get((String.format("%s//weather?lat=55.87&lon=26.52", URL))))
                .andDo(print())
                .andExpect(status().is5xxServerError())
                .andExpect(content().string(containsString("placeholder")));
    }

    @Test
    public void WhenSavingValidPlanOnDayWithAlerts_ShouldReturnEntityAndListOfAlerts()  throws Exception{

        String requestBody =
                (String.format("{\"latitude\": %s,\"longitude\": %s,\"planDate\":\"%s\"}",
                        latitude, longitude, date.toString()));

        OutdoorActivity outdoorActivity = new OutdoorActivity(latitude, longitude, date);
        OutdoorPlanResponse expected = new OutdoorPlanResponse(outdoorActivity, alerts);

        when(outdoorPlanServiceMock.saveOutdoorPlan(outdoorActivity))
                .thenReturn(expected);

        this.mockMvc
                .perform(post(String.format("%s//activity", URL))
                        .content(requestBody)
                        .header("Content-Type", "application/json"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Yellow Flooding Warning")));
    }

    @Test
    public void WhenSavingValidPlanOnDayWithoutAlerts_ShouldReturnEntityAndEmptyAlertList()  throws Exception{

        String requestBody =
                (String.format("{\"latitude\": %s,\"longitude\": %s,\"planDate\":\"%s\"}",
                        latitude, longitude, date.toString()));

        OutdoorActivity outdoorActivity = new OutdoorActivity(latitude, longitude, date);
        OutdoorPlanResponse expected = new OutdoorPlanResponse(outdoorActivity, null);

        when(outdoorPlanServiceMock.saveOutdoorPlan(outdoorActivity))
                .thenReturn(expected);

        this.mockMvc
                .perform(post(String.format("%s//activity", URL))
                        .content(requestBody)
                        .header("Content-Type", "application/json"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("planDate")));
    }

    @Test
    public void WhenSavingPlanOnDayBeforePrognosisRange_ShouldThrowPastDateException()  throws Exception{

        String requestBody =
                (String.format("{\"latitude\": %s,\"longitude\": %s,\"planDate\":\"%s\"}",
                        latitude, longitude, date.toString()));

        OutdoorActivity outdoorActivity = new OutdoorActivity(latitude, longitude, date);

        when(outdoorPlanServiceMock.saveOutdoorPlan(outdoorActivity))
                .thenThrow(new PastDateException("placeholder"));

        this.mockMvc
                .perform(post(String.format("%s//activity", URL))
                        .content(requestBody)
                        .header("Content-Type", "application/json"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("placeholder")));
    }

    @Test
    public void WhenSavingPlanWithInvalidCoordinates_ShouldThrowMethodArgumentNotValidException()  throws Exception{

        String requestBody =
                (String.format("{\"latitude\": %s,\"longitude\": %s,\"planDate\":\"%s\"}",
                        latitude, "555", date.toString()));

        this.mockMvc
                .perform(post(String.format("%s//activity", URL))
                        .content(requestBody)
                        .header("Content-Type", "application/json"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Longitude must be less")));
    }

    @Test
    public void WhenSavingPlanWithArgumentMissing_ShouldThrowMethodArgumentNotValidException()  throws Exception{

        String requestBody =
                (String.format("{\"latitude\": %s,\"planDate\":\"%s\"}",
                        latitude, date.toString()));

        this.mockMvc
                .perform(post(String.format("%s//activity", URL))
                        .content(requestBody)
                        .header("Content-Type", "application/json"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Longitude value should not be empty")));
    }

}