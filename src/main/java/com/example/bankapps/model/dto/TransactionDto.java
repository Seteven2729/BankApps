package com.example.bankapps.model.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionDto {
    private Long id;
    private String type;
    private String amount;
    private String balanceAfter;
    private LocalDateTime createdAt;
}
