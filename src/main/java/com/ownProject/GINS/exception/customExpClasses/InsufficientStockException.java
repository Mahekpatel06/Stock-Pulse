package com.ownProject.GINS.exception.customExpClasses;

public class InsufficientStockException extends RuntimeException {
    public InsufficientStockException(String message) {
        super(message);
    }
}
