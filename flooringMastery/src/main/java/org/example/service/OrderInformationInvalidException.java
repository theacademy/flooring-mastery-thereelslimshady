package org.example.service;

public class OrderInformationInvalidException extends RuntimeException {
    public OrderInformationInvalidException(String message) {
        super(message);
    }
    public OrderInformationInvalidException(String message, Throwable cause) {
        super(message, cause);
    }

}
