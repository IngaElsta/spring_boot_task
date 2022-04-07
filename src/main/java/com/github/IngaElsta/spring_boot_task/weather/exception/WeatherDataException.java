package com.github.IngaElsta.spring_boot_task.weather.exception;

public abstract class WeatherDataException extends RuntimeException {
    public WeatherDataException(String message) {
        super(message);
    }
}
