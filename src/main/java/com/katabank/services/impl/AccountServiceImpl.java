package com.katabank.services.impl;

import com.katabank.dto.AccountDTO;
import com.katabank.dto.AccountRequestDTO;
import com.katabank.entity.Account;
import com.katabank.entity.Transaction;
import com.katabank.exception.AccountException;
import com.katabank.exception.NotFoundException;
import com.katabank.mapper.AccountMapper;
import com.katabank.repository.AccountRepository;
import com.katabank.services.AccountService;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final AccountMapper mapperAccount =  Mappers.getMapper(AccountMapper.class);

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public AccountDTO save(AccountRequestDTO accountRequestDTO) {
        accountRepository.findByCbuCvu(accountRequestDTO.getCbuCvu())
                .ifPresent(account -> {
                    throw new AccountException("Error: The account with the given CBU/CVU already exists.");
                });

        Account accountEntity = mapperAccount.toEntity(accountRequestDTO);
        Account savedEntity = accountRepository.save(accountEntity);
        return mapperAccount.toDTO(savedEntity);
    }


    @Override
    public List<AccountDTO> getAll() {
        return mapperAccount.toAccountDTOs(accountRepository.findAll());
    }

}
