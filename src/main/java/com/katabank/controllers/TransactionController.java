package com.katabank.controllers;

import com.katabank.dto.ErrorDTO;
import com.katabank.dto.TransactionDTO;
import com.katabank.dto.TransferRequestDTO;
import com.katabank.entity.Transaction;
import com.katabank.services.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@RestController
@RequestMapping("/banking/v1")
@Tag(name = "Transaction", description = "API for handling transactions, including fund transfers between accounts.")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Operation(summary = "Transfer funds between accounts", description = "Transfers funds from one account to another with the provided transfer details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Funds transferred successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Transaction.class))
            }),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))
            }),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))
            })
    })
    @PostMapping("/transactions/transfer")
    public ResponseEntity<TransactionDTO> transferFundsBetweenAccounts(@RequestBody @Valid TransferRequestDTO transferRequestDTO) {
        var transaction = transactionService.transferFundsBetweenAccounts(transferRequestDTO);
        return ResponseEntity.ok(transaction);
    }

    @GetMapping("/transactions/transfer")
    @Operation(summary = "Get all transactions", description = "Retrieve a list of all transactions.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of transactions retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))
            })
    })
    public ResponseEntity<List<TransactionDTO>> getAccounts() {
        return new ResponseEntity<>(
                transactionService.getTransactions(),
                HttpStatus.OK
        );
    }
}