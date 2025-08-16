package com.example.bankapps.repository;

import com.example.bankapps.model.dao.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account,Long> {
}
