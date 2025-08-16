package com.example.bankapps.unitTest.service;

import com.example.bankapps.exception.AccountNotFoundException;
import com.example.bankapps.exception.InsufficientBalanceException;
import com.example.bankapps.model.dao.Account;
import com.example.bankapps.model.dao.Transaction;
import com.example.bankapps.model.dto.StatementDto;
import com.example.bankapps.model.dto.TransactionRequest;
import com.example.bankapps.repository.AccountRepository;
import com.example.bankapps.repository.TransactionRepository;
import com.example.bankapps.service.impl.TransactionServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private TransactionRepository transactionRepository;
    @InjectMocks
    private TransactionServiceImpl transactionService;


    @Test
    void withdrawMoneySuccess() {
        when(accountRepository.findByEmailForUpdate(anyString())).thenReturn(Optional.ofNullable(Account.builder()
                .email("email")
                .id(1L)
                .balance(BigDecimal.valueOf(5000))
                .transactions(List.of(new Transaction())).build()));

        when(transactionRepository.findByRequestIdAndType(anyString(), anyString())).thenReturn(Optional.empty());
        when(transactionRepository.save(any(Transaction.class))).thenReturn(new Transaction());
        when(accountRepository.save(any(Account.class))).thenReturn(Account.builder()
                .email("email")
                .id(1L)
                .balance(BigDecimal.valueOf(5000))
                .transactions(List.of(new Transaction())).build());

        Account account = transactionService.withdrawMoney(new TransactionRequest("easd", "1", BigDecimal.valueOf(40)));

        assertEquals("email", account.getEmail());

        verify(accountRepository).findByEmailForUpdate(anyString());
        verify(transactionRepository).findByRequestIdAndType(anyString(), anyString());
        verify(transactionRepository).save(any(Transaction.class));
        verify(accountRepository).save(any(Account.class));
    }

    @Test
    void withdrawMoneyIdempotency() {
        when(accountRepository.findByEmailForUpdate(anyString())).thenReturn(Optional.ofNullable(Account.builder()
                .email("email")
                .id(1L)
                .balance(BigDecimal.valueOf(5000))
                .transactions(List.of(new Transaction())).build()));

        when(transactionRepository.findByRequestIdAndType(anyString(), anyString())).thenReturn(Optional.ofNullable(Transaction.builder().build()));


        Account account = transactionService.withdrawMoney(new TransactionRequest("easd", "1", BigDecimal.valueOf(40)));

        assertEquals("email", account.getEmail());

        verify(accountRepository).findByEmailForUpdate(anyString());
        verify(transactionRepository).findByRequestIdAndType(anyString(), anyString());
    }

    @Test
    void withdrawMoneyInsufficientBalance() {
        when(accountRepository.findByEmailForUpdate(anyString())).thenReturn(Optional.ofNullable(Account.builder()
                .email("email")
                .id(1L)
                .balance(BigDecimal.valueOf(5000))
                .transactions(List.of(new Transaction())).build()));

        when(transactionRepository.findByRequestIdAndType(anyString(), anyString())).thenReturn(Optional.empty());

        TransactionRequest request = new TransactionRequest("easd", "1", BigDecimal.valueOf(5001));
        InsufficientBalanceException ex = assertThrows(InsufficientBalanceException.class, () -> {
            transactionService.withdrawMoney(request);
        });

        assertNotNull(ex.getMessage());

        verify(accountRepository).findByEmailForUpdate(anyString());
        verify(transactionRepository).findByRequestIdAndType(anyString(), anyString());
    }


    @Test
    void depositMoneySuccess() {
        when(accountRepository.findByEmailForUpdate(anyString())).thenReturn(Optional.ofNullable(Account.builder()
                .email("email")
                .id(1L)
                .balance(BigDecimal.valueOf(5000))
                .transactions(List.of(new Transaction())).build()));

        when(transactionRepository.findByRequestIdAndType(anyString(), anyString())).thenReturn(Optional.empty());
        when(transactionRepository.save(any(Transaction.class))).thenReturn(new Transaction());
        when(accountRepository.save(any(Account.class))).thenReturn(Account.builder()
                .email("email")
                .id(1L)
                .balance(BigDecimal.valueOf(5000))
                .transactions(List.of(new Transaction())).build());

        Account account = transactionService.depositMoney(new TransactionRequest("easd", "1", BigDecimal.valueOf(40)));

        assertEquals("email", account.getEmail());

        verify(accountRepository).findByEmailForUpdate(anyString());
        verify(transactionRepository).findByRequestIdAndType(anyString(), anyString());
        verify(transactionRepository).save(any(Transaction.class));
        verify(accountRepository).save(any(Account.class));
    }

    @Test
    void depositMoneyIdempotency() {
        when(accountRepository.findByEmailForUpdate(anyString())).thenReturn(Optional.ofNullable(Account.builder()
                .email("email")
                .id(1L)
                .balance(BigDecimal.valueOf(5000))
                .transactions(List.of(new Transaction())).build()));

        when(transactionRepository.findByRequestIdAndType(anyString(), anyString())).thenReturn(Optional.ofNullable(Transaction.builder().build()));


        Account account = transactionService.depositMoney(new TransactionRequest("easd", "1", BigDecimal.valueOf(40)));

        assertEquals("email", account.getEmail());

        verify(accountRepository).findByEmailForUpdate(anyString());
        verify(transactionRepository).findByRequestIdAndType(anyString(), anyString());
    }

    @Test
    void getBalanceSuccess(){
        when(accountRepository.findByEmail(anyString())).thenReturn(Optional.ofNullable(Account.builder()
                .email("email")
                .id(1L)
                .balance(BigDecimal.valueOf(5000))
                .transactions(List.of(new Transaction())).build()));

        String balance = transactionService.getBalance("email");

        assertNotNull(balance,"not null");
        verify(accountRepository).findByEmail(anyString());
    }

    @Test
    void getBalanceFailed(){
        when(accountRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        AccountNotFoundException ex = assertThrows(AccountNotFoundException.class, () -> {
            transactionService.getBalance("any");
        });
        assertNotNull(ex.getMessage(),"not null");
        verify(accountRepository).findByEmail(anyString());
    }

    @Test
    void getStatementSuccess(){
        String email = "test@example.com";

        Account account = Account.builder()
                .id(1L)
                .email(email)
                .balance(new BigDecimal("1000"))
                .build();
        when(accountRepository.findByEmail(email)).thenReturn(Optional.of(account));

        Transaction tx1 = Transaction.builder()
                .id(1L)
                .type("DEPOSIT")
                .amount(new BigDecimal("500"))
                .balanceAfter(new BigDecimal("500"))
                .createdAt(LocalDateTime.of(2025, 8, 16, 10, 0))
                .build();

        Transaction tx2 = Transaction.builder()
                .id(2L)
                .type("WITHDRAW")
                .amount(new BigDecimal("200"))
                .balanceAfter(new BigDecimal("300"))
                .createdAt(LocalDateTime.of(2025, 8, 16, 12, 0))
                .build();

        List<Transaction> transactions = List.of(tx2, tx1);
        when(transactionRepository.findByAccountIdOrderByCreatedAtDesc(account.getId()))
                .thenReturn(transactions);

        StatementDto statement = transactionService.getStatement(email);

        assertNotNull(statement);
        assertEquals(email, statement.getEmail());
        assertEquals("Rp1.000", statement.getBalance());
        assertEquals(2, statement.getTransactions().size());

        assertTrue(statement.getTransactions().get(0).getCreatedAt().isBefore(
                statement.getTransactions().get(1).getCreatedAt()));

        verify(accountRepository).findByEmail(email);
        verify(transactionRepository).findByAccountIdOrderByCreatedAtDesc(account.getId());
    }

    @Test
    void testGetStatementAccountNotFound() {
        String email = "missing@example.com";
        when(accountRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> transactionService.getStatement(email));
        verify(accountRepository).findByEmail(email);
        verifyNoInteractions(transactionRepository);
    }
}
