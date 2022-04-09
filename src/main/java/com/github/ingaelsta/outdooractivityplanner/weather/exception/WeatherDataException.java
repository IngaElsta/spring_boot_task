package com.github.ingaelsta.outdooractivityplanner.weather.exception;

public abstract class WeatherDataException extends RuntimeException {
    public WeatherDataException(String message) {
        super(message);
    }
}
