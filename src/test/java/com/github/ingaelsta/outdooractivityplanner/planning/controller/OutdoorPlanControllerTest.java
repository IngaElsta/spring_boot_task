package com.github.ingaelsta.outdooractivityplanner.planning.controller;

import com.github.ingaelsta.outdooractivityplanner.commons.Conversion;
import com.github.ingaelsta.outdooractivityplanner.planning.model.OutdoorActivitiesLocation;
import com.github.ingaelsta.outdooractivityplanner.planning.service.OutdoorPlanService;
import com.github.ingaelsta.outdooractivityplanner.weather.entity.Temperature;
import com.github.ingaelsta.outdooractivityplanner.weather.entity.WeatherConditions;
import com.github.ingaelsta.outdooractivityplanner.weather.entity.Wind;

import com.github.ingaelsta.outdooractivityplanner.weather.exception.WeatherDataException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    private OutdoorPlanService outdoorActivityPlanningServiceMock;

    @Test
    public void WhenNoParametersPassedToGetWeather_ShouldUseDefaultValuesAndReturnData() throws Exception {
        LocalDate date = Conversion.convertDate(1643536800).toLocalDate();;
        Temperature temperature = new Temperature(1.64, 1.09, -0.16, -0.94);
        Wind wind = new Wind(8.23, 17.56, "S");
        List<String> weatherDescriptions = new ArrayList<>();
        weatherDescriptions.add("rain and snow");

        Map<LocalDate, WeatherConditions> expected = new HashMap<>();
        expected.put(date, new WeatherConditions(
                date, weatherDescriptions, temperature, wind, new ArrayList<>()));

        when(outdoorActivityPlanningServiceMock.getWeather(new OutdoorActivitiesLocation(56.95, 24.11))).thenReturn(expected);

        this.mockMvc
                .perform(get("/api/v1/outdoor-planner/weather"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("rain and snow")));
    }

    @Test
    public void WhenValidLocationPassedToGetWeather_ShouldUsePassedValuesAndReturnData() throws Exception {
        LocalDate date = Conversion.convertDate(1643536800).toLocalDate();;
        Temperature temperature = new Temperature(1.64, 1.09, -0.16, -0.94);
        Wind wind = new Wind(8.23, 17.56, "S");
        List<String> weatherDescriptions = new ArrayList<>();
        weatherDescriptions.add("rain and snow");

        Map<LocalDate, WeatherConditions> expected = new HashMap<>();
        expected.put(date, new WeatherConditions(
                date, weatherDescriptions, temperature, wind, new ArrayList<>()));

        when(outdoorActivityPlanningServiceMock.getWeather(new OutdoorActivitiesLocation(55.87, 26.52))).thenReturn(expected);

        this.mockMvc
                .perform(get("/api/v1/outdoor-planner/weather?lat=55.87&lon=26.52"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("rain and snow")));
    }

    @Test
    public void WhenInvalidLocationPassedToGetWeather_ShouldReturnError() throws Exception {
        this.mockMvc
                .perform(get("/api/v1/outdoor-planner/weather?lat=555&lon=26.52"))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void WhenWeatherDataRetrievalUnsuccessful_GetWeatherShouldReturnError() throws Exception {
        when(outdoorActivityPlanningServiceMock.getWeather(new OutdoorActivitiesLocation(55.87, 26.52)))
                .thenThrow(new WeatherDataException("placeholder") {});

        this.mockMvc
                .perform(get("/api/v1/outdoor-planner/weather?lat=55.87&lon=26.52"))
                .andDo(print())
                .andExpect(status().is5xxServerError())
                .andExpect(content().string(containsString("placeholder")));
    }

}