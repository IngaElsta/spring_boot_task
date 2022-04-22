package com.github.ingaelsta.outdooractivityplanner.planning.exception;

public class PastDateException extends RuntimeException {
    public PastDateException(String message) {
        super(message);
    }
}
