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

public class OWMDeserializer extends StdDeserializer<WeatherConditionsMapWrapper> {

    public OWMDeserializer() {
        this(null);
    }

    public OWMDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public WeatherConditionsMapWrapper deserialize(
            JsonParser parser, DeserializationContext deserializer) throws IOException {

        WeatherConditionsMapWrapper conditionsMap = new WeatherConditionsMapWrapper(new HashMap<LocalDate, WeatherConditions>());

        ObjectCodec codec = parser.getCodec();
        JsonNode node = codec.readTree(parser);

        //todo: add logic here

        return conditionsMap;
    }
}
