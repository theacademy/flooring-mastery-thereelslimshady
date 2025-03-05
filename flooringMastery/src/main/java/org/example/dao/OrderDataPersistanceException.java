package org.example.dao;

public class OrderDataPersistanceException extends Exception {
    public OrderDataPersistanceException(String message) {
        super(message);
    }
    public OrderDataPersistanceException(String message, Throwable cause) {
        super(message, cause);
    }

}
