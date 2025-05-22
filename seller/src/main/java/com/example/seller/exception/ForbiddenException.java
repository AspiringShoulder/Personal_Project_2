package com.example.seller.exception;

public class ForbiddenException extends RuntimeException
{
    public ForbiddenException(String message) {
        super(message);
    }
}
