package com.github.IngaElsta.spring_boot_task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OWMInitializer {

    private final OWMConfiguration owmConfiguration;
    private static final Logger LOGGER =
            LoggerFactory.getLogger(OWMInitializer.class);

    @Autowired
    OWMInitializer(OWMConfiguration owmConfiguration) {
        this.owmConfiguration = owmConfiguration;
        LOGGER.info("Connection to OWM initialized");
    }

}
