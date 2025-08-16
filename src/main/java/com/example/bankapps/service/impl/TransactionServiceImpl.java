package com.example.bankapps.service.impl;

import com.example.bankapps.commons.Utility;
import com.example.bankapps.exception.AccountNotFoundException;
import com.example.bankapps.exception.InsufficientBalanceException;
import com.example.bankapps.model.dao.Account;
import com.example.bankapps.model.dao.Transaction;
import com.example.bankapps.model.dto.TransactionRequest;
import com.example.bankapps.repository.AccountRepository;
import com.example.bankapps.repository.TransactionRepository;
import com.example.bankapps.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;


    @Override
    @Transactional
    public Account withdrawMoney(TransactionRequest request) {
        Account account = accountRepository.findByEmailForUpdate(request.getEmail()).orElseThrow(() -> new AccountNotFoundException(request.getEmail()));

        // (idempotency)
        if (transactionRepository.findByRequestId(request.getReqId()).isPresent()) {
            return account;
        }
        if (account.getBalance().compareTo(request.getAmount()) < 0) {
            throw new InsufficientBalanceException(Utility.toIDR(account.getBalance()),Utility.toIDR(request.getAmount()));
        }
        account.setBalance(account.getBalance().subtract(request.getAmount()));

        Transaction txn = Transaction.builder()
                .account(account)
                .type("WITHDRAW")
                .amount(request.getAmount())
                .balanceAfter(account.getBalance())
                .requestId(request.getReqId())
                .build();
        transactionRepository.save(txn);

        return accountRepository.save(account);
    }

    @Override
    @Transactional
    public String depositMoney(TransactionRequest request) {
        return "";
    }

    @Override
    public String getStatement() {
        return "";
    }

    @Override
    public String getBalance(String email) {
        BigDecimal currBalance = accountRepository.findByEmail(email).orElseThrow(() -> new AccountNotFoundException(email)).getBalance();
        return Utility.toIDR(currBalance);
    }

}
