package com.github.ingaelsta.outdooractivityplanner.commons.response;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;

@Data
public class ErrorResponse {
    private HttpStatus status;
    private List<String> messages;
    private String stackTrace;

    public ErrorResponse(HttpStatus status, List<String> messages) {
        this.status = status;
        this.messages = messages;
    }

    public ErrorResponse(HttpStatus status, List<String> messages, String stackTrace) {
        this.status = status;
        this.messages = messages;
        this.stackTrace = stackTrace;
    }
}






