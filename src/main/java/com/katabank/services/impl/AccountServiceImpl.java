package com.katabank.services.impl;

import com.katabank.dto.AccountDTO;
import com.katabank.dto.AccountRequestDTO;
import com.katabank.entity.Account;
import com.katabank.exception.AccountException;
import com.katabank.mapper.AccountMapper;
import com.katabank.repository.AccountRepository;
import com.katabank.services.AccountService;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import java.util.List;

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

        var accountEntity = mapperAccount.toEntity(accountRequestDTO);
        var savedEntity = accountRepository.save(accountEntity);
        return mapperAccount.toDTO(savedEntity);
    }


    @Override
    public List<AccountDTO> getAll() {
        return mapperAccount.toAccountDTOs(accountRepository.findAll());
    }

}
