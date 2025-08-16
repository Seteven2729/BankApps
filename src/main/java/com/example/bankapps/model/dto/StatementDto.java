package com.example.bankapps.model.dto;

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
