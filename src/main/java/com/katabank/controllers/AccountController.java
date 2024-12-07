package com.katabank.controllers;

import com.katabank.dto.AccountDTO;
import com.katabank.dto.AccountRequestDTO;
import com.katabank.dto.MovementDTO;
import com.katabank.entity.Transaction;
import com.katabank.services.AccountService;
import com.katabank.services.MovementService;
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
@Tag(name = "Account", description = "API for managing user accounts, including account creation, balance inquiry, and transaction history.")
@RequestMapping("/banking/v1/accounts")
public class AccountController {

    private final AccountService accountService;
    private final MovementService movementService;

    public AccountController(AccountService accountService, MovementService movementService) {
        this.accountService = accountService;
        this.movementService = movementService;
    }

    @PostMapping
    public ResponseEntity<AccountDTO> saveAccount(@RequestBody @Valid AccountRequestDTO accountRequestDTO) {
        AccountDTO accountDTO = accountService.save(accountRequestDTO);
        return new ResponseEntity<>(
                accountDTO,
                HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<AccountDTO>> getAccounts(){
        return new ResponseEntity<>(
                accountService.getAll(),
                HttpStatus.OK);
    }

    @GetMapping("/{cbuCvu}/transactions")
    public ResponseEntity<List<Transaction>> getAccountTransactions(
            @PathVariable String cbuCvu,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {
        return null;
    }
    @GetMapping("/{accountId}/movements")
    public ResponseEntity<List<MovementDTO>> getAccountMovements(
            @PathVariable Long accountId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        List<MovementDTO> movements = movementService.getMovements(accountId, startDate, endDate);
        return new ResponseEntity<>(movements, HttpStatus.OK);
    }
}
