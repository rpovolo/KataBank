package com.katabank.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Setter
@Getter
public class TransferRequestDTO {

    @NotNull(message = "The source account CBU/CVU is required")
    @Size(min = 22, max = 22, message = "The CBU/CVU must have 22 characters")
    private String source;

    @NotNull(message = "The destination account CBU/CVU is required")
    @Size(min = 22, max = 22, message = "The CBU/CVU must have 22 characters")
    private String destination;

    @NotNull(message = "The amount is required")
    @Positive(message = "The amount must be positive")
    private BigDecimal amount;

    @NotNull(message = "Transaction Detail")
    private String description;

}
