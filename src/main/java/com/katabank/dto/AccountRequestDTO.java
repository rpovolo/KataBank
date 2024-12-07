package com.katabank.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Setter
@Getter
public class AccountRequestDTO {

    @NotNull(message = "The CBU/CVU is required")
    @Size(min = 22, max = 22, message = "The CBU/CVU must be exactly 22 characters long")
    @Pattern(regexp = "\\d{22}", message = "The CBU/CVU must contain only numeric digits")
    private String cbuCvu;

    @NotNull(message = "The balance is required")
    @Positive(message = "The balance must be positive")
    private BigDecimal balance;
}
