package com.katabank.services;

import com.katabank.entity.Account;
import com.katabank.entity.Movement;
import com.katabank.entity.Transaction;
import com.katabank.enun.MovementType;
import com.katabank.repository.AccountRepository;
import com.katabank.repository.MovementRepository;
import com.katabank.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

@Service
public class AccountOperationsServiceImpl implements AccountOperationsService{
    private final MovementRepository movementRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private static final Logger logger = LoggerFactory.getLogger(AccountOperationsServiceImpl.class);

    public AccountOperationsServiceImpl(MovementRepository movementRepository, AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.movementRepository = movementRepository;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void startConsole() {
        initialSave();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            logger.info("Ingrese el CBU/CVU de la cuenta (o 'exit' para salir):");
            String cbuCvu = scanner.nextLine();

            if ("exit".equalsIgnoreCase(cbuCvu)) {
                logger.info("Saliendo del sistema...");
                break;
            }

            logger.info("¿Cómo desea ordenar los movimientos?");
            logger.info("1. Por fecha");
            logger.info("2. Por monto");
            int sortChoice = scanner.nextInt();
            logger.info("¿Desea orden ascendente o descendente?");
            logger.info("1. Ascendente");
            logger.info("2. Descendente");
            int orderChoice = scanner.nextInt();
            scanner.nextLine();

            showMovements(cbuCvu, sortChoice, orderChoice);
        }
    }

    private void showMovements(String cbuCvu, int sortChoice, int orderChoice) {
        var account = accountRepository.findByCbuCvu(cbuCvu);

        if (account.isEmpty()) {
            logger.warn("Cuenta no encontrada.");
            return;
        }

        var movements = movementRepository.findByAccount(cbuCvu);
        if (movements.isEmpty()) {
            logger.warn("No hay movimientos registrados para esta cuenta.");
            return;
        }

        if (sortChoice == 1) {
            if (orderChoice == 1) {
                movements.sort(Comparator.comparing(Movement::getCreatedAt));
            } else {
                movements.sort(Comparator.comparing(Movement::getCreatedAt).reversed());
            }
        } else if (sortChoice == 2) {
            if (orderChoice == 1) {
                movements.sort(Comparator.comparing(Movement::getAmount));
            } else {
                movements.sort(Comparator.comparing(Movement::getAmount).reversed());
            }
        }

        logMovements(cbuCvu, movements);
    }

    private void logMovements(String cbuCvu, List<Movement> movements) {
        logger.info("Cuenta: {}", cbuCvu);
        logger.info("-------------------------------------------------------------------------------------------------");
        logger.info(String.format("%-12s %-30s %-15s %-15s", "Fecha", "Descripción", "Monto", "Saldo"));
        logger.info("-------------------------------------------------------------------------------------------------");

        var dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (Movement movement : movements) {
            var date = movement.getCreatedAt().toLocalDate().format(dateFormatter);
            var description = movement.getTransaction().getDescription();
            var amount = (movement.getMovementType() == MovementType.DEBIT ? "-" : "") + "$" + movement.getAmount();
            var balance = "$" + movement.getBalanceAfter();

            logger.info(String.format("%-12s %-30s %-15s %-15s", date, description, amount, balance));
        }
        logger.info("-------------------------------------------------------------------------------------------------");
    }

    private void initialSave() {
        var account1 = new Account();
        account1.setCbuCvu("6569285692392351272280");
        account1.setBalance(new BigDecimal("1200.00"));
        accountRepository.save(account1);

        var account2 = new Account();
        account2.setCbuCvu("6569285692392351272279");
        account2.setBalance(new BigDecimal("800.00"));
        accountRepository.save(account2);

        var transaction = new Transaction();
        transaction.setAmount(new BigDecimal("200.00"));
        transaction.setDescription("Pago de servicios");
        transaction.setDestinationAccount(account2);
        transaction.setOriginAccount(account1);
        transaction.setTransactionDate(LocalDateTime.now());
        transactionRepository.save(transaction);

        var movement1 = new Movement();
        movement1.setAccount(account1);
        movement1.setAmount(new BigDecimal("-200.00"));
        movement1.setBalanceAfter(new BigDecimal("800.00"));
        movement1.setCreatedAt(LocalDateTime.now());
        movement1.setMovementType(MovementType.DEBIT);
        movement1.setTransaction(transaction);
        movementRepository.save(movement1);

        var movement2 = new Movement();
        movement2.setAccount(account2);
        movement2.setAmount(new BigDecimal("200.00"));
        movement2.setCreatedAt(LocalDateTime.now());
        movement2.setMovementType(MovementType.CREDIT);
        movement2.setTransaction(transaction);
        movement2.setBalanceAfter(new BigDecimal("1200.00"));
        movementRepository.save(movement2);
    }
}
