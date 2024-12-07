package com.katabank.repository;

import com.katabank.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByCbuCvu(String cbuCvu);
}
