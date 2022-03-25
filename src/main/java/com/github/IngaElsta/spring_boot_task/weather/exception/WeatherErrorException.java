package com.github.IngaElsta.spring_boot_task.weather.exception;

public abstract class WeatherErrorException extends Exception {
    private int ReturnedErrorCode;
    private String ReturnedErrorMessage;

    public WeatherErrorException (String message, int errorCode, String errorMessage) {
        super(message);

        this.ReturnedErrorCode = errorCode;
        this.ReturnedErrorMessage = errorMessage;
    }

    public WeatherErrorException(String message) {
        super(message);
    }
}
