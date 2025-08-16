package com.example.bankapps.exception;

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(String email) {
        super("Account not found for email: " + email);
    }
}