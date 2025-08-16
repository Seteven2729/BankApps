package com.example.bankapps.exception;

public class InsufficientBalanceException extends RuntimeException {
    public InsufficientBalanceException(String currentBalance, String requestedAmount) {
        super("Insufficient balance. Current: " + currentBalance + ", Requested: " + requestedAmount);
    }
}
