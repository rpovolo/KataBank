package com.katabank.services.impl;

import com.katabank.dto.MovementDTO;
import com.katabank.entity.Movement;
import com.katabank.repository.MovementRepository;
import com.katabank.services.MovementService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovementServiceImpl implements MovementService {

    private final MovementRepository movementRepository;

    public MovementServiceImpl(MovementRepository movementRepository) {
        this.movementRepository = movementRepository;
    }

    @Override
    public List<MovementDTO> getMovements(String account, LocalDateTime startDate, LocalDateTime endDate) {
        List<Movement> movements;

        if (startDate != null && endDate != null) {
            movements = movementRepository.findByAccountAndCreatedAtBetween(account, startDate, endDate);
        }
        else if (startDate != null) {
            movements = movementRepository.findByAccountAndCreatedAtAfter(account, startDate);
        }
        else if (endDate != null) {
            movements = movementRepository.findByAccountAndCreatedAtBefore(account, endDate);
        }
        else {
            movements = movementRepository.findByAccount(account);
        }

        return movements.stream()
                .map(this::convertToDTO)
                .toList();
    }


    private MovementDTO convertToDTO(Movement movement) {
        MovementDTO dto = new MovementDTO();
        dto.setMovementId(movement.getMovementId());
        dto.setTransactionId(movement.getTransaction().getId());
        dto.setAccountId(movement.getAccount().getId());
        dto.setMovementType(movement.getMovementType());
        dto.setAmount(movement.getAmount());
        dto.setBalanceAfter(movement.getBalanceAfter());
        dto.setCreatedAt(movement.getCreatedAt());
        return dto;
    }
}
