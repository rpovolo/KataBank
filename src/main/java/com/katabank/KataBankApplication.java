package com.katabank;

import com.katabank.entity.Account;
import com.katabank.entity.Movement;
import com.katabank.entity.Transaction;
import com.katabank.enun.MovementType;
import com.katabank.repository.AccountRepository;
import com.katabank.repository.MovementRepository;
import com.katabank.repository.TransactionRepository;
import com.katabank.services.AccountOperationsService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@SpringBootApplication
public class KataBankApplication implements CommandLineRunner {

    private final AccountOperationsService accountOperationsService;

    public KataBankApplication(AccountOperationsService accountOperationsService) {
        this.accountOperationsService = accountOperationsService;
    }

    public static void main(String[] args) {
        SpringApplication.run(KataBankApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        accountOperationsService.startConsole();
    }
}
