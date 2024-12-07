package com.katabank.controllers;

import com.katabank.dto.TransferRequestDTO;
import com.katabank.entity.Transaction;
import com.katabank.exception.NotFoundException;
import com.katabank.repository.AccountRepository;
import com.katabank.repository.TransactionRepository;
import com.katabank.services.TransactionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/banking/v1")
@Tag(name = "Transaction", description = "API for handling transactions, including fund transfers between accounts.")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/transactions/transfer")
    public ResponseEntity<String> transferFundsBetweenAccounts(@RequestBody @Valid TransferRequestDTO transferRequestDTO) {
        transactionService.transferFundsBetweenAccounts(transferRequestDTO);
        return ResponseEntity.ok("Transfer completed successfully");
    }
}