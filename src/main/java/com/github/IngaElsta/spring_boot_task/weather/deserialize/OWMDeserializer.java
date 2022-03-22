package com.github.IngaElsta.spring_boot_task.weather.deserialize;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.github.IngaElsta.spring_boot_task.weather.domain.*;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

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

        Map<LocalDate, WeatherConditions> conditionsMap = new HashMap<LocalDate, WeatherConditions>();

        ObjectCodec codec = parser.getCodec();
        JsonNode node = codec.readTree(parser);

        System.out.println(node);

        //todo: add logic here

        LocalDate date = Instant.ofEpochSecond(1643536800).atZone(ZoneId.systemDefault()).toLocalDate();
        Temperature temperature = new Temperature("1.64", "1.09", "-0.16", "-0.94");
        Wind wind = new Wind("8.23", "17.56", "S");
        WeatherConditions conditions = new WeatherConditions(date, "rain and snow", temperature, wind, null);

        conditionsMap.put(date, conditions);

        return conditionsMap;
    }
}
