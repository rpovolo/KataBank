package com.katabank.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
@Entity
@Data
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String cbuCvu;

    @Column(nullable = false)
    private BigDecimal balance;
}
