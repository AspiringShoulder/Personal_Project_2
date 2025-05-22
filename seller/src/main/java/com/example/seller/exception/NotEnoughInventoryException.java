package com.example.seller.exception;

public class NotEnoughInventoryException extends RuntimeException
{
    public NotEnoughInventoryException(String message) {
        super(message);
    }
}
