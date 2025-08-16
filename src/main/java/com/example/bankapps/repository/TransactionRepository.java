package com.example.bankapps.repository;

import com.example.bankapps.model.dao.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction,Long> {
    Optional<Transaction> findByRequestIdAndType(String requestId, String type);;
}
