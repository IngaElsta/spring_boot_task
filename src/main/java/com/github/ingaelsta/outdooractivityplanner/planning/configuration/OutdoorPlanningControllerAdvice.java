package com.github.ingaelsta.outdooractivityplanner.planning.configuration;

import com.github.ingaelsta.outdooractivityplanner.planning.response.ErrorResponse;
import com.github.ingaelsta.outdooractivityplanner.weather.exception.WeatherDataException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.io.PrintWriter;
import java.io.StringWriter;

@RestControllerAdvice
public class OutdoorPlanningControllerAdvice {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(Exception e) {
        return new ResponseEntity<ErrorResponse>(new ErrorResponse(
                HttpStatus.BAD_REQUEST, e.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(WeatherDataException.class)
    public ResponseEntity<ErrorResponse> HandleWeatherDataException(Exception e) {
        return new ResponseEntity<ErrorResponse>(new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // fallback method
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleExceptions(Exception e) {

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        e.printStackTrace(printWriter);
        String stackTrace = stringWriter.toString();

        return new ResponseEntity<>(
                new ErrorResponse(status, e.getMessage(),stackTrace),
                status
        );
    }
}