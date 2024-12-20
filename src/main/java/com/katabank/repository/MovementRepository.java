package com.katabank.repository;

import com.katabank.entity.Movement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MovementRepository extends JpaRepository<Movement, Long> {
    @Query("SELECT m FROM Movement m WHERE m.account.cbuCvu = :account AND m.createdAt BETWEEN :startDate AND :endDate")
    List<Movement> findByAccountAndCreatedAtBetween(String account, LocalDateTime startDate, LocalDateTime endDate);
    @Query("SELECT m FROM Movement m WHERE m.account.cbuCvu = :account AND m.createdAt > :startDate")
    List<Movement> findByAccountAndCreatedAtAfter(String account, LocalDateTime startDate);
    @Query("SELECT m FROM Movement m WHERE m.account.cbuCvu = :account AND m.createdAt < :endDate")
    List<Movement> findByAccountAndCreatedAtBefore(String account, LocalDateTime endDate);
    @Query("SELECT m FROM Movement m WHERE m.account.cbuCvu = :account")
    List<Movement> findByAccount(String account);

}
