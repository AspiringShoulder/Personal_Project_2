package com.example.seller.exception;

public class OrderStatusException extends RuntimeException
{
    public OrderStatusException(String message)
    {
        super(message);
    }
}
