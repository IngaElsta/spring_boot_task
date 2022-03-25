package com.github.IngaElsta.spring_boot_task.weather.exception;

public class OWMErrorException extends WeatherErrorException {

    public OWMErrorException(String body) {
        super(body);
    }

    public OWMErrorException(String message, int errorCode, String errorMessage) {
        super(message, errorCode, errorMessage);
    }
}
