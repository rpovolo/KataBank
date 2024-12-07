package com.katabank.services;

import com.katabank.dto.AccountDTO;
import com.katabank.dto.AccountRequestDTO;

import java.util.List;

public interface AccountService {
        AccountDTO save (AccountRequestDTO accountDTO);
        List<AccountDTO> getAll();
}
