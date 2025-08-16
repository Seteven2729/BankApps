package com.example.bankapps.model.dao;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "accounts")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "email", unique = true)
    private String email;

    @Builder.Default
    @Column(name = "balance", nullable = false, columnDefinition = "numeric default 500000")
    private BigDecimal balance = BigDecimal.valueOf(500000);
}
