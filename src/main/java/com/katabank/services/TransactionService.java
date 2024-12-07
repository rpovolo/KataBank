package com.katabank.services;

import com.katabank.dto.TransferRequestDTO;
import com.katabank.entity.Transaction;

import java.time.LocalDate;
import java.util.List;


public interface TransactionService {
    List<Transaction> getTransactions(String cbuCvu, LocalDate startDate, LocalDate endDate);
    void transferFundsBetweenAccounts(TransferRequestDTO transferRequestDTO);
}
