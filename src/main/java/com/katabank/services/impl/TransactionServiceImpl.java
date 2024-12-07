package com.katabank.services.impl;

import com.katabank.dto.TransferRequestDTO;
import com.katabank.entity.Movement;
import com.katabank.entity.Transaction;
import com.katabank.exception.InternalErrorException;
import com.katabank.exception.NotFoundException;
import com.katabank.repository.AccountRepository;
import com.katabank.repository.MovementRepository;
import com.katabank.repository.TransactionRepository;
import com.katabank.services.TransactionService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import com.katabank.enun.MovementType;
@Service
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final MovementRepository movementRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository, AccountRepository accountRepository, MovementRepository movementRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.movementRepository = movementRepository;
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

            Transaction transaction = new Transaction();
            transaction.setOriginAccount(sourceAccount);
            transaction.setDestinationAccount(destinationAccount);
            transaction.setAmount(transferRequestDTO.getAmount());
            transaction.setDescription(transferRequestDTO.getDescription());
            transaction.setTransactionDate(LocalDateTime.now());


            Transaction transactionCreated =transactionRepository.save(transaction);

            Movement movement1 = new Movement();
            movement1.setTransaction(transactionCreated);
            movement1.setAccount(sourceAccount);
            movement1.setMovementType(MovementType.DEBIT);
            movement1.setAmount(transferRequestDTO.getAmount().negate());
            movement1.setBalanceAfter(sourceAccount.getBalance());
            movement1.setCreatedAt(LocalDateTime.now());

            movementRepository.save(movement1);

            Movement movement2 = new Movement();
            movement2.setTransaction(transactionCreated);
            movement2.setAccount(destinationAccount);
            movement2.setMovementType(MovementType.CREDIT);
            movement2.setAmount(transferRequestDTO.getAmount());
            movement2.setBalanceAfter(destinationAccount.getBalance());
            movement2.setCreatedAt(LocalDateTime.now());

            movementRepository.save(movement2);

            accountRepository.save(sourceAccount);
            accountRepository.save(destinationAccount);

        }catch (Exception e){
            throw new InternalErrorException("Unexpected error: " + e.getMessage());
        }
    }

}
