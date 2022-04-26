package com.github.ingaelsta.outdooractivityplanner.weather.controller;

import com.github.ingaelsta.outdooractivityplanner.commons.Conversion;
import com.github.ingaelsta.outdooractivityplanner.commons.model.Location;
import com.github.ingaelsta.outdooractivityplanner.weather.exception.WeatherDataException;
import com.github.ingaelsta.outdooractivityplanner.weather.model.Temperature;
import com.github.ingaelsta.outdooractivityplanner.weather.model.WeatherConditions;
import com.github.ingaelsta.outdooractivityplanner.weather.model.Wind;
import com.github.ingaelsta.outdooractivityplanner.weather.service.WeatherDataService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WeatherController.class)
class WeatherControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WeatherDataService weatherDataServiceMock;
    private static final String URL = "/api/v1/outdoor-planner/weather";
    private static final LocalDate date = Conversion.convertDate(1643536800).toLocalDate();

    //get weather
    @Test
    public void WhenNoParametersPassedToGetWeather_thenUsesDefaultValuesAndReturnsData() throws Exception {
        Temperature temperature = new Temperature(1.64, 1.09, -0.16, -0.94);
        Wind wind = new Wind(8.23, 17.56, "S");
        List<String> weatherDescriptions = new ArrayList<>();
        weatherDescriptions.add("rain and snow");

        Map<LocalDate, WeatherConditions> expected = new HashMap<>();
        expected.put(date, new WeatherConditions(
                date, weatherDescriptions, temperature, wind, new ArrayList<>()));

        when(weatherDataServiceMock.retrieveWeather(new Location(56.95, 24.11)))
                .thenReturn(expected);

        this.mockMvc
                .perform(get(URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("rain and snow")));
    }

    @Test
    public void WhenValidLocationPassedToGetWeather_thenUsesPassedValuesAndReturnsData() throws Exception {
        Temperature temperature = new Temperature(1.64, 1.09, -0.16, -0.94);
        Wind wind = new Wind(8.23, 17.56, "S");
        List<String> weatherDescriptions = new ArrayList<>();
        weatherDescriptions.add("rain and snow");

        Map<LocalDate, WeatherConditions> expected = new HashMap<>();
        expected.put(date, new WeatherConditions(
                date, weatherDescriptions, temperature, wind, new ArrayList<>()));

        when(weatherDataServiceMock.retrieveWeather(new Location(55.87, 26.52)))
                .thenReturn(expected);

        this.mockMvc
                .perform(get((String.format("%s?lat=55.87&lon=26.52", URL))))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("rain and snow")));
    }

    @Test
    public void WhenInvalidLocationPassedToGetWeather_thenReturnError() throws Exception {
        this.mockMvc
                .perform(get((String.format("%s?lat=555&lon=26.52", URL))))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("must be less than")));
    }

    @Test
    public void WhenWeatherDataRetrievalUnsuccessful_thenReturnError() throws Exception {
        when(weatherDataServiceMock.retrieveWeather(new Location(55.87, 26.52)))
                .thenThrow(new WeatherDataException("placeholder") {});

        this.mockMvc
                .perform(get((String.format("%s?lat=55.87&lon=26.52", URL))))
                .andDo(print())
                .andExpect(status().is5xxServerError())
                .andExpect(content().string(containsString("placeholder")));
    }
}