package com.example.bankapps.model.dto;

import com.example.bankapps.model.dao.Account;
import com.example.bankapps.model.dao.Transaction;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatementDto {
    private String email;
    private String balance;
    private List<TransactionDto> transactions;
}
