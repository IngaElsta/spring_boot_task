package com.github.IngaElsta.spring_boot_task.weather.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration ("openweathermap")
@ConfigurationProperties(prefix = "openweathermap")
public class OWMConfiguration {

    private String authToken;
    private String oneApiUrl;

}
