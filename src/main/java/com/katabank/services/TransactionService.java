package com.katabank.services;

import com.katabank.dto.TransactionDTO;
import com.katabank.dto.TransferRequestDTO;
import java.util.List;

public interface TransactionService {
    List<TransactionDTO> getTransactions();
    TransactionDTO transferFundsBetweenAccounts(TransferRequestDTO transferRequestDTO);
}
