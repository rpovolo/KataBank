package com.katabank.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
@Entity
@Getter
@Setter
@NoArgsConstructor  // Lombok genera un constructor sin parámetros
@AllArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String cbuCvu;

    @Column(nullable = false)
    private BigDecimal balance;
}
