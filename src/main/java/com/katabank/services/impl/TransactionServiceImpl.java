package com.katabank.services.impl;

import com.katabank.dto.TransferRequestDTO;
import com.katabank.entity.Transaction;
import com.katabank.enun.TransactionType;
import com.katabank.exception.InternalErrorException;
import com.katabank.exception.NotFoundException;
import com.katabank.repository.AccountRepository;
import com.katabank.repository.TransactionRepository;
import com.katabank.services.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
@Service
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public List<Transaction> getTransactions(String cbuCvu, LocalDate startDate, LocalDate endDate) {
        return null;
    }

    @Override
    public void transferFundsBetweenAccounts(TransferRequestDTO transferRequestDTO) {

        var sourceAccount = accountRepository.findByCbuCvu(transferRequestDTO.getSource())
                .orElseThrow(
                        () -> new NotFoundException("Soruce account not Found"));

        var destinationAccount = accountRepository.findByCbuCvu(transferRequestDTO.getDestination())
                .orElseThrow(
                        () -> new NotFoundException("Destination account not Found"));

        if (sourceAccount.getBalance().compareTo(transferRequestDTO.getAmount()) < 0) {
            throw new InternalErrorException("Insufficient funds in the source account");
        }

        try {
            sourceAccount.setBalance(sourceAccount.getBalance().subtract(transferRequestDTO.getAmount()));
            destinationAccount.setBalance(destinationAccount.getBalance().add(transferRequestDTO.getAmount()));

            var transaction = Transaction.builder()
                    .originAccount(sourceAccount)
                    .destinationAccount(destinationAccount)
                    .amount(transferRequestDTO.getAmount())
                    .description(transferRequestDTO.getDescription())
                    .transactionDate(LocalDateTime.now())
                    .build();



            accountRepository.save(sourceAccount);
            accountRepository.save(destinationAccount);
            transactionRepository.save(transaction);
        }catch (Exception e){
            throw new InternalErrorException("Unexpected error: " + e.getMessage());
        }
    }

}
