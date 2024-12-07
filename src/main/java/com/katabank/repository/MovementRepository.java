package com.katabank.repository;

import com.katabank.entity.Account;
import com.katabank.entity.Movement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MovementRepository extends JpaRepository<Movement, Long> {
    @Query("SELECT m FROM Movement m WHERE m.account.cbuCvu = :account AND m.createdAt BETWEEN :startDate AND :endDate")
    List<Movement> findByAccountIdAndCreatedAtBetween(String account, LocalDateTime startDate, LocalDateTime endDate);
    List<Movement> findByAccountIdAndCreatedAtAfter(String accountId, LocalDateTime startDate);
    List<Movement> findByAccountIdAndCreatedAtBefore(String accountId, LocalDateTime endDate);
    List<Movement> findByAccountId(String accountId);

}
