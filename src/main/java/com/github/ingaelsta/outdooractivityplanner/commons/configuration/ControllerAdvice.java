package com.github.ingaelsta.outdooractivityplanner.commons.configuration;

import com.github.ingaelsta.outdooractivityplanner.planning.exception.PastDateException;
import com.github.ingaelsta.outdooractivityplanner.commons.response.ErrorResponse;
import com.github.ingaelsta.outdooractivityplanner.weather.exception.WeatherDataException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolationException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(Exception e) {
        List<String> errors = Arrays.asList(e.getMessage()
                .replace("getWeather.", "")
                .split(", "));

        return new ResponseEntity<>(new ErrorResponse(
                HttpStatus.BAD_REQUEST, errors),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        List<String> errors = new ArrayList<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String errorMessage = error.getDefaultMessage();
            errors.add(errorMessage);
        });
        return new ResponseEntity<>(
                new ErrorResponse(HttpStatus.BAD_REQUEST, errors),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({MissingServletRequestParameterException.class,
            PastDateException.class,
            MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ErrorResponse> HandlePastDateException(Exception e) {
        List<String> errors = new ArrayList<>();
        errors.add (e.getMessage());

        return new ResponseEntity<>(new ErrorResponse(
                HttpStatus.BAD_REQUEST, errors),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(WeatherDataException.class)
    public ResponseEntity<ErrorResponse> HandleWeatherDataException(Exception e) {
        List<String> errors = new ArrayList<>();
        errors.add (e.getMessage());

        return new ResponseEntity<>(new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR, errors),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // fallback method
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleExceptions(Exception e) {

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        e.printStackTrace(printWriter);
        String stackTrace = stringWriter.toString(); //todo: if multiple env, make this dev only otherwise null

        List<String> errors = new ArrayList<>();
        errors.add (e.getMessage());

        return new ResponseEntity<>(
                new ErrorResponse(status, errors, stackTrace),
                status
        );
    }
}