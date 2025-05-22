package com.example.seller.exception;

public class OrderNotFoundException extends RuntimeException
{
    public OrderNotFoundException()
    {
        super("Cannot find order with this ID.");
    }
}
