package org.example.dao;

import java.io.FileNotFoundException;

public class ProductDataPersistanceException extends Throwable {
    public ProductDataPersistanceException(String message) {
        super(message);
    }
    public ProductDataPersistanceException(String message, Throwable cause) {
        super(message, cause);
    }
}
