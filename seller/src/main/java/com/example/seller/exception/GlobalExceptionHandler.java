package com.example.seller.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler
{
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<String> handleInvalidCredentialsException(InvalidCredentialsException ice)
    {
        return ResponseEntity.status(401).body(ice.getMessage());
    }

    @ExceptionHandler(NotEnoughInventoryException.class)
    public ResponseEntity<String> handleNotEnoughInventoryException(NotEnoughInventoryException neie)
    {
        return ResponseEntity.status(400).body(neie.getMessage());
    }

    @ExceptionHandler(OrderStatusException.class)
    public ResponseEntity<String> handleOrderStatusException(OrderStatusException ose)
    {
        return ResponseEntity.status(400).body(ose.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException unfe)
    {
        return ResponseEntity.status(404).body(unfe.getMessage());
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<String> handleProductNotFoundException(ProductNotFoundException pnfe)
    {
        return ResponseEntity.status(404).body(pnfe.getMessage());
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<String> handleOrderNotFoundException(OrderNotFoundException e)
    {
        return ResponseEntity.status(404).body(e.getMessage());
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<String> handleForbiddenException(ForbiddenException e)
    {
        return ResponseEntity.status(403).body(e.getMessage());
    }
}
