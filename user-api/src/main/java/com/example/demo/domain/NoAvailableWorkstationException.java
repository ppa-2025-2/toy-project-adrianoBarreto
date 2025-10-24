package com.example.demo.domain;

public class NoAvailableWorkstationException extends RuntimeException {
    public NoAvailableWorkstationException(String message) {
        super(message);
    }
}