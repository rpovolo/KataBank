package com.katabank.dto;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Setter
@Getter
public class AccountDTO {

    private String cbuCvu;

    private BigDecimal balance;
}
