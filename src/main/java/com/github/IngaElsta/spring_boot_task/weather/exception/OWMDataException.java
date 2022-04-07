package com.github.IngaElsta.spring_boot_task.weather.exception;

public class OWMDataException extends WeatherDataException {

    public OWMDataException(String body) {
        super(body);
    }

    public OWMDataException(String message, int errorCode, String errorMessage) {
        super(message, errorCode, errorMessage);
    }
}
