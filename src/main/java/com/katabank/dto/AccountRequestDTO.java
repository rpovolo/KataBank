package com.katabank.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Setter
@Getter
@EqualsAndHashCode
public class AccountRequestDTO {

    @NotNull(message = "The CBU/CVU is required")
    @Size(min = 22, max = 22, message = "The CBU/CVU must be exactly 22 characters long")
    @Pattern(regexp = "\\d{22}", message = "The CBU/CVU must contain only numeric digits")
    private String cbuCvu;

    @NotNull(message = "The balance is required")
    @Positive(message = "The balance must be positive")
    private BigDecimal balance;
}
