package com.katabank.services;

import com.katabank.dto.MovementDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface MovementService {
    List<MovementDTO> getMovements(Long accountId, LocalDateTime startDate, LocalDateTime endDate);
}
