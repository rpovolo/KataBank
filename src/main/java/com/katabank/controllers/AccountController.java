package com.katabank.controllers;

import com.katabank.dto.AccountDTO;
import com.katabank.dto.AccountRequestDTO;
import com.katabank.dto.ErrorDTO;
import com.katabank.dto.MovementDTO;
import com.katabank.services.AccountService;
import com.katabank.services.MovementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @Operation(summary = "Create a new account", description = "Creates a new bank account with the provided details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Account created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))
            }),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))
            })
    })
    public ResponseEntity<AccountDTO> saveAccount(@RequestBody @Valid AccountRequestDTO accountRequestDTO) {
        var accountDTO = accountService.save(accountRequestDTO);
        return new ResponseEntity<>(
                accountDTO,
                HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all accounts", description = "Retrieve a list of all user accounts.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of accounts retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))
            })
    })
    public ResponseEntity<List<AccountDTO>> getAccounts() {
        return new ResponseEntity<>(
                accountService.getAll(),
                HttpStatus.OK
        );
    }

    @GetMapping("/{account}/movements")
    @Operation(summary = "Get account movements", description = "Retrieve account movements within a specified date range.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Movements retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Account not found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))
            }),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))
            })
    })
    public ResponseEntity<List<MovementDTO>> getAccountMovements(
            @PathVariable
            @Parameter(description = "Account identifier") String account,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            @Parameter(description = "Start date in UTC for the movements filter, in ISO 8601 format (e.g., 2024-12-05T17:10:04.879+00:00) (optional).") LocalDateTime startDate,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            @Parameter(description = "End date in UTC for the movements filter, in ISO 8601 format (e.g., 2024-12-05T17:10:04.879+00:00) (optional).") LocalDateTime endDate) {

        var movements = movementService.getMovements(account, startDate, endDate);
        return new ResponseEntity<>(movements, HttpStatus.OK);
    }
}
