package com.github.IngaElsta.spring_boot_task.weather.exception;

public abstract class WeatherDataException extends RuntimeException {
    private int ReturnedErrorCode;
    private String ReturnedErrorMessage;

    public WeatherDataException(String message, int errorCode, String errorMessage) {
        super(message);

        this.ReturnedErrorCode = errorCode;
        this.ReturnedErrorMessage = errorMessage;
    }

    public WeatherDataException(String message) {
        super(message);
    }
}
