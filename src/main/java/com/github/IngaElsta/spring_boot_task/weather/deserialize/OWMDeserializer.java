package com.github.IngaElsta.spring_boot_task.weather.deserialize;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.github.IngaElsta.spring_boot_task.weather.domain.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class OWMDeserializer extends StdDeserializer<Map<LocalDate, WeatherConditions>> {

    public OWMDeserializer() {
        this(null);
    }

    public OWMDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Map<LocalDate, WeatherConditions> deserialize(
            JsonParser parser, DeserializationContext deserializer) throws IOException {

        Map<LocalDate, WeatherConditions> conditionsMap = new HashMap<>();

        ObjectCodec codec = parser.getCodec();
        JsonNode node = codec.readTree(parser);
        JsonNode dailyNode = node.get("daily");

        if (dailyNode.isArray()) {
            dailyNode.forEach(dailyWeather -> {
                try {
                    LocalDate date = WeatherConditions.convertDate(dailyWeather.get("dt").asLong()).toLocalDate();

                    JsonNode temperatureNode = dailyWeather.get("temp");
                    Temperature temperature = new Temperature(
                            temperatureNode.get("morn").asText(),
                            temperatureNode.get("day").asText(),
                            temperatureNode.get("eve").asText(),
                            temperatureNode.get("night").asText()
                    );

                    Wind wind = new Wind(
                            dailyWeather.get("wind_speed").asText(),
                            dailyWeather.get("wind_gust").asText(),
                            Wind.degreesToDirection(dailyWeather.get("wind_deg").asInt()));

                    JsonNode weatherNode = dailyWeather.get("weather");

                    List<String> weatherDescriptions = new ArrayList<>();
                    weatherNode.forEach(description -> {
                        weatherDescriptions.add(description.get("description").asText());
                    });

                    WeatherConditions conditions = new WeatherConditions(
                            date, weatherDescriptions, temperature, wind, null);

                    conditionsMap.put(date, conditions);
                } catch (NullPointerException e) {
                    log.error("NullPointerException while processing {}", dailyWeather);
                    throw e;
                }
            });
        }

        return conditionsMap;
    }
}
