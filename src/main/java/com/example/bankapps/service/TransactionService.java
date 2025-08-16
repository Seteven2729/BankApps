package com.example.bankapps.service;

import com.example.bankapps.model.dao.Account;
import com.example.bankapps.model.dto.StatementDto;
import com.example.bankapps.model.dto.TransactionRequest;

public interface TransactionService {
    Account withdrawMoney(TransactionRequest request);
    Account depositMoney(TransactionRequest request);
    StatementDto getStatement(String email);
    String getBalance(String email);
}
