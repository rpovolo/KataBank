package com.katabank.mapper;

import com.katabank.dto.TransactionDTO;
import com.katabank.entity.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper(uses = {AccountMapper.class})
public interface TransactionMapper {

    @Mapping(source = "originAccount.cbuCvu", target = "originAccount")
    @Mapping(source = "destinationAccount.cbuCvu", target = "destinationAccount")
    TransactionDTO toDTO(Transaction transaction);

    @Mapping(source = "originAccount", target = "originAccount.cbuCvu")
    @Mapping(source = "destinationAccount", target = "destinationAccount.cbuCvu")
    Transaction toEntity(TransactionDTO transactionDTO);

    List<Transaction> toTransactionEntities(List<TransactionDTO> transactionDTOs);
    List<TransactionDTO> toTransactionDTOs(List<Transaction> transactions);
}
