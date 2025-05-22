package com.example.seller.exception;

public class UserNotFoundException extends RuntimeException
{
    public UserNotFoundException() {
        super("No user with this ID was found.");
    }
}
