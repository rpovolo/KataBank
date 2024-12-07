package com.katabank.controllers;

import com.katabank.dto.AccountDTO;
import com.katabank.dto.AccountRequestDTO;
import com.katabank.services.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/banking/v1")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/accounts")
    public ResponseEntity<AccountDTO> saveAccount(@RequestBody @Valid AccountRequestDTO accountRequestDTO) {
        AccountDTO accountDTO = accountService.save(accountRequestDTO);
        return new ResponseEntity<>(
                accountDTO,
                HttpStatus.CREATED);
    }

    @GetMapping("/accounts")
    public ResponseEntity<List<AccountDTO>> getAccounts(){
        return new ResponseEntity<>(
                accountService.getAll(),
                HttpStatus.OK);
    }
}
