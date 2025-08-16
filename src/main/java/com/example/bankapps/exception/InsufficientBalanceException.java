package com.example.bankapps.exception;

import java.math.BigDecimal;

public class InsufficientBalanceException extends RuntimeException {
    public InsufficientBalanceException(String currentBalance, String requestedAmount) {
        super("Insufficient balance. Current: " + currentBalance + ", Requested: " + requestedAmount);
    }
}
