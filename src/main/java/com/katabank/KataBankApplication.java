package com.katabank;

import com.katabank.services.AccountOperationsService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
           accountOperationsService.recordTransactions();
    }
}
