package com.github.IngaElsta.spring_boot_task.planning.controller;

import com.github.IngaElsta.spring_boot_task.planning.domain.SkiLocation;
import com.github.IngaElsta.spring_boot_task.planning.service.SkiPlanService;
import com.github.IngaElsta.spring_boot_task.weather.domain.Temperature;
import com.github.IngaElsta.spring_boot_task.weather.domain.WeatherConditions;
import com.github.IngaElsta.spring_boot_task.weather.domain.Wind;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.HttpClientErrorException;

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

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(SkiPlanController.class)
class SkiPlanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SkiPlanService service;

    @Test
    public void WhenNoParametersPassedToGetWeather_ShouldUseDefaultValuesAndReturnData() throws Exception {
        LocalDate date = WeatherConditions.convertDate(1643536800).toLocalDate();;
        Temperature temperature = new Temperature(1.64, 1.09, -0.16, -0.94);
        Wind wind = new Wind(8.23, 17.56, "S");
        List<String> weatherDescriptions = new ArrayList<>();
        weatherDescriptions.add("rain and snow");

        Map<LocalDate, WeatherConditions> expected = new HashMap<>();
        expected.put(date, new WeatherConditions(
                date, weatherDescriptions, temperature, wind, new ArrayList<>()));

        when(service.getWeather(new SkiLocation("56.95", "24.11"))).thenReturn(expected);

        this.mockMvc
                .perform(get("/api/v1/ski-planner/weather"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("rain and snow")));
    }

    @Test
    public void WhenValidLocationPassedToGetWeather_ShouldUsePassedValuesAndReturnData() throws Exception {
        LocalDate date = WeatherConditions.convertDate(1643536800).toLocalDate();;
        Temperature temperature = new Temperature(1.64, 1.09, -0.16, -0.94);
        Wind wind = new Wind(8.23, 17.56, "S");
        List<String> weatherDescriptions = new ArrayList<>();
        weatherDescriptions.add("rain and snow");

        Map<LocalDate, WeatherConditions> expected = new HashMap<>();
        expected.put(date, new WeatherConditions(
                date, weatherDescriptions, temperature, wind, new ArrayList<>()));

        when(service.getWeather(new SkiLocation("55.87", "26.52"))).thenReturn(expected);

        this.mockMvc
                .perform(get("/api/v1/ski-planner/weather?lat=55.87&lon=26.52"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("rain and snow")));
    }

    @Test
    public void WhenInvalidLocationPassedToGetWeather_ShouldReturnError() throws Exception {
        this.mockMvc
                .perform(get("/api/v1/ski-planner/weather?lat=555&lon=26.52"))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

}