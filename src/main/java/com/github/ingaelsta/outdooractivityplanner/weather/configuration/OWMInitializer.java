package com.github.ingaelsta.outdooractivityplanner.weather.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class OWMInitializer {

    private final OWMConfiguration owmConfiguration;

    @Autowired
    OWMInitializer(OWMConfiguration owmConfiguration) {
        this.owmConfiguration = owmConfiguration;
        log.info("Connection to OWM initialized");
    }

}
