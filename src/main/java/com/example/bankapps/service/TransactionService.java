package com.example.bankapps.service;

import com.example.bankapps.model.dao.Account;
import com.example.bankapps.model.dto.TransactionRequest;

public interface TransactionService {
    Account withdrawMoney(TransactionRequest request);
    String depositMoney(TransactionRequest request);
    String getStatement();
    String getBalance(String email);
}
