package com.katabank.services.impl;

import com.katabank.dto.TransactionDTO;
import com.katabank.dto.TransferRequestDTO;
import com.katabank.entity.Movement;
import com.katabank.entity.Transaction;
import com.katabank.entity.Account;
import com.katabank.enun.MovementType;
import com.katabank.exception.InternalErrorException;
import com.katabank.exception.NotFoundException;
import com.katabank.mapper.AccountMapper;
import com.katabank.mapper.TransactionMapper;
import com.katabank.repository.AccountRepository;
import com.katabank.repository.MovementRepository;
import com.katabank.repository.TransactionRepository;
import com.katabank.services.TransactionService;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final MovementRepository movementRepository;
    private final TransactionMapper transactionMapper =  Mappers.getMapper(TransactionMapper.class);

    public TransactionServiceImpl(TransactionRepository transactionRepository, AccountRepository accountRepository, MovementRepository movementRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.movementRepository = movementRepository;
    }

    @Override
    public List<TransactionDTO> getTransactions() {
        return transactionMapper.toTransactionDTOs(transactionRepository.findAll());
    }

    @Override
    public TransactionDTO transferFundsBetweenAccounts(TransferRequestDTO transferRequestDTO) {
        var sourceAccount = validateAndGetAccount(transferRequestDTO.getSource(), "Source account not found");
        var destinationAccount = validateAndGetAccount(transferRequestDTO.getDestination(), "Destination account not found");

        validateSufficientFunds(sourceAccount, transferRequestDTO.getAmount());

        try {
            return processTransfer(sourceAccount, destinationAccount, transferRequestDTO);
        } catch (Exception e) {
            throw new InternalErrorException("Unexpected error: " + e.getMessage());
        }
    }

    private Account validateAndGetAccount(String cbuCvu, String errorMessage) {
        return accountRepository.findByCbuCvu(cbuCvu)
                .orElseThrow(() -> new NotFoundException(errorMessage));
    }

    private void validateSufficientFunds(Account sourceAccount, BigDecimal amount) {
        if (sourceAccount.getBalance().compareTo(amount) < 0) {
            throw new InternalErrorException("Insufficient funds in the source account");
        }
    }

    private TransactionDTO processTransfer(Account sourceAccount, Account destinationAccount, TransferRequestDTO transferRequestDTO) {

        sourceAccount.setBalance(sourceAccount.getBalance().subtract(transferRequestDTO.getAmount()));
        destinationAccount.setBalance(destinationAccount.getBalance().add(transferRequestDTO.getAmount()));

        Transaction transaction = createTransaction(sourceAccount, destinationAccount, transferRequestDTO);
        transaction = transactionRepository.save(transaction);

        createAndSaveMovements(transaction, sourceAccount, destinationAccount, transferRequestDTO);

        accountRepository.save(sourceAccount);
        accountRepository.save(destinationAccount);

        return transactionMapper.toDTO(transaction);
    }

    private Transaction createTransaction(Account sourceAccount, Account destinationAccount, TransferRequestDTO transferRequestDTO) {
        Transaction transaction = new Transaction();
        transaction.setOriginAccount(sourceAccount);
        transaction.setDestinationAccount(destinationAccount);
        transaction.setAmount(transferRequestDTO.getAmount());
        transaction.setDescription(transferRequestDTO.getDescription());
        transaction.setTransactionDate(LocalDateTime.now());
        return transaction;
    }

    private void createAndSaveMovements(Transaction transaction, Account sourceAccount, Account destinationAccount, TransferRequestDTO transferRequestDTO) {
        // Debit movement
        Movement debitMovement = createMovement(transaction, sourceAccount, MovementType.DEBIT, transferRequestDTO.getAmount().negate(), sourceAccount.getBalance());
        movementRepository.save(debitMovement);

        // Credit movement
        Movement creditMovement = createMovement(transaction, destinationAccount, MovementType.CREDIT, transferRequestDTO.getAmount(), destinationAccount.getBalance());
        movementRepository.save(creditMovement);
    }

    private Movement createMovement(Transaction transaction, Account account, MovementType movementType, BigDecimal amount, BigDecimal balanceAfter) {
        Movement movement = new Movement();
        movement.setTransaction(transaction);
        movement.setAccount(account);
        movement.setMovementType(movementType);
        movement.setAmount(amount);
        movement.setBalanceAfter(balanceAfter);
        movement.setCreatedAt(LocalDateTime.now());
        return movement;
    }
}
