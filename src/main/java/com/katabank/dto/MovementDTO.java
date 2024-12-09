package com.katabank.dto;

import com.katabank.enun.MovementType;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
public class MovementDTO {

    private Long movementId;
    private Long transactionId;
    private Long accountId;
    private MovementType movementType;
    private BigDecimal amount;
    private BigDecimal balanceAfter;
    private LocalDateTime createdAt;

}
