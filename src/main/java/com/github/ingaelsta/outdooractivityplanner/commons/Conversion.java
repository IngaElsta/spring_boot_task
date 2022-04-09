package com.github.ingaelsta.outdooractivityplanner.commons;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class Conversion {
    public static LocalDateTime convertDate(long date_seconds){
        Instant instant = Instant.ofEpochSecond(date_seconds);
        return instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
}
