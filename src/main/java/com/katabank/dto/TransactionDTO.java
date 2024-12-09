package com.katabank.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionDTO {
    private String originAccount;
    private String destinationAccount;
    private LocalDateTime transactionDate;
    private String description;
    private BigDecimal amount;
}
