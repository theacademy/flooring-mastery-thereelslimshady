package org.example.service;

public class ProductInformationInvalidException extends Exception {
    public ProductInformationInvalidException(String message) {
        super(message);
    }
    public ProductInformationInvalidException(String message, Throwable cause) {
        super(message, cause);
    }

}
