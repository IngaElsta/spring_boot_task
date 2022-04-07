package com.github.IngaElsta.spring_boot_task.commons;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ConversionTest {

    @Test
    void ConversionFromSecondsToDateTimeIsSuccessful() {
        LocalDateTime expectedDateTime = LocalDateTime.of(2022, 01, 28,12,0);
        assertEquals(Conversion.convertDate(1643364000), expectedDateTime);
    }
}