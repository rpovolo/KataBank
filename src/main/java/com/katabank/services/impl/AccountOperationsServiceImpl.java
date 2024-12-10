package com.katabank.services.impl;

import com.katabank.dto.TransferRequestDTO;
import com.katabank.entity.Account;
import com.katabank.entity.Movement;
import com.katabank.enun.MovementType;
import com.katabank.repository.AccountRepository;
import com.katabank.repository.MovementRepository;
import com.katabank.services.AccountOperationsService;
import com.katabank.services.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

@Service
public class AccountOperationsServiceImpl implements AccountOperationsService {
    private final MovementRepository movementRepository;
    private final AccountRepository accountRepository;
    private final TransactionService transactionService;
    private final Environment environment;
    private static final Logger logger = LoggerFactory.getLogger(AccountOperationsServiceImpl.class);

    public AccountOperationsServiceImpl(MovementRepository movementRepository, AccountRepository accountRepository, TransactionService transactionService, Environment environment) {
        this.movementRepository = movementRepository;
        this.accountRepository = accountRepository;
        this.transactionService = transactionService;
        this.environment = environment;
    }

    @Override
    public void recordTransactions () {

        String[] activeProfiles = environment.getActiveProfiles();
        var isDevProfile = false;

        for (String profile : activeProfiles) {
            if ("dev".equals(profile)) {
                isDevProfile = true;
                break;
            }
        }

        if (!isDevProfile) {
            logger.info("El perfil 'dev' no está activo. No se cargan transacciones de pruebas....");
            return;
        }

        recordTransaction();

        var scanner = new Scanner(System.in);
        while (true) {
            logger.info("Ingrese el CBU/CVU de la cuenta (o 'exit' para salir):");
            var cbuCvu = scanner.nextLine();

            if ("exit".equalsIgnoreCase(cbuCvu)) {
                logger.info("Saliendo del sistema...");
                break;
            }

            logger.info("¿Cómo desea ordenar los movimientos?");
            logger.info("1. Por fecha");
            logger.info("2. Por monto");
            var sortChoice = scanner.nextInt();
            logger.info("¿Desea orden ascendente o descendente?");
            logger.info("1. Ascendente");
            logger.info("2. Descendente");
            var orderChoice = scanner.nextInt();
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

        String logMessage = String.format("""
            Cuenta: %s
            -------------------------------------------------------------------------------------------------
            %-12s %-30s %-15s %-15s
            -------------------------------------------------------------------------------------------------
        """, cbuCvu, "Fecha", "Descripción", "Monto", "Saldo");

        logger.info(logMessage);

        var dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        movements.stream()
                .map(movement -> {
                    var date = movement.getCreatedAt().toLocalDate().format(dateFormatter);
                    var description = movement.getMovementType();
                    var amount = (movement.getMovementType() == MovementType.DEBIT ? "-" : "") + "$" + movement.getAmount();
                    var balance = "$" + movement.getBalanceAfter();

                    return String.format("%-12s %-30s %-15s %-15s", date, description, amount, balance);
                })
                .forEach(logger::info);

        logger.info("-------------------------------------------------------------------------------------------------");
    }

    private void recordTransaction() {

        for (int i = 0; i < 40; i++) {
            var account = new Account();
            account.setCbuCvu(generateRandomCvu());
            account.setBalance(new BigDecimal("120000.00"));
            accountRepository.save(account);
        }

        processAccountPairs(accountRepository.findAll());
    }

    private String generateRandomCvu() {

        var random = new Random();
        var prefix = String.format("%08d", random.nextInt(100000000));
        var uniqueAccount = String.format("%014d", Math.abs(random.nextLong() % 100000000000000L));
        return prefix + uniqueAccount;

    }

    public void processAccountPairs(List<Account> accounts) {
        var transactionDate = LocalDateTime.now();
        var transactionCounter = 0;

        var limitedAccounts = accounts.subList(0, 5);

        for (Account originAccount : limitedAccounts) {
            for (int i = 0; i < 10; i++) {
                for (Account destinationAccount : limitedAccounts) {
                    if (!originAccount.getCbuCvu().equals(destinationAccount.getCbuCvu())) {
                        var transferRequestDTO = new TransferRequestDTO();
                        transferRequestDTO.setSource(originAccount.getCbuCvu());
                        transferRequestDTO.setDestination(destinationAccount.getCbuCvu());
                        transferRequestDTO.setAmount(new BigDecimal("200.00"));
                        transferRequestDTO.setDescription("Pago de servicios");
                        transactionService.transferFundsBetweenAccounts(transferRequestDTO);

                        logger.info(String.format(
                                "Transacción realizada entre las cuentas %s (origen) y %s (destino) en la fecha %s",
                                originAccount.getCbuCvu(),
                                destinationAccount.getCbuCvu(),
                                transactionDate
                        ));

                        transactionCounter++;

                        if (transactionCounter % 5 == 0) {
                            transactionDate = transactionDate.plusDays(1);
                        }
                    }
                }
            }
        }
    }

}
