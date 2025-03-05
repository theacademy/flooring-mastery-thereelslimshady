package org.example.dao;

import java.io.FileNotFoundException;

public class TaxDataPersistanceException extends Exception {
    public TaxDataPersistanceException(String message) {
        super(message);
    }
    public TaxDataPersistanceException(String message, Throwable cause) {
        super(message, cause);
    }
}
