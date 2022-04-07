package com.github.IngaElsta.spring_boot_task.planning.response;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ErrorResponse {
    private HttpStatus status;
    private String message;
    private String stackTrace;

    public ErrorResponse(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public ErrorResponse(HttpStatus status, String message, String stackTrace) {
        this.status = status;
        this.message = message;
        this.stackTrace = stackTrace;
    }
}






