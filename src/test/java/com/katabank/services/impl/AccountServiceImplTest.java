package com.katabank.services.impl;

import com.katabank.dto.AccountDTO;
import com.katabank.dto.AccountRequestDTO;
import com.katabank.entity.Account;
import com.katabank.exception.AccountException;
import com.katabank.mapper.AccountMapper;
import com.katabank.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class AccountServiceImplTest {
    @Autowired
    private AccountServiceImpl accountService;
    @MockBean
    private AccountRepository accountRepository;
    @MockBean
    private AccountMapper accountMapper;
    public static final String CBU = "2850590940090418135201";
    public static final String CBU_2 = "2850590940090418135200";

    @Test
    void testSave_accountAlreadyExists_throwsException() {

        AccountRequestDTO accountRequestDTO = new AccountRequestDTO();
        accountRequestDTO.setCbuCvu(CBU);
        when(accountRepository.findByCbuCvu(CBU)).thenReturn(Optional.of(new Account()));
        assertThrows(AccountException.class, () -> accountService.save(accountRequestDTO));
        verify(accountRepository, never()).save(any(Account.class));

    }

    @Test
    void testSave_validAccount_savesAndReturnsDTO() {
        AccountRequestDTO accountRequestDTO = new AccountRequestDTO();
        accountRequestDTO.setCbuCvu(CBU);

        Account accountEntity = new Account();
        accountEntity.setCbuCvu(CBU);

        Account savedEntity = new Account();
        savedEntity.setCbuCvu(CBU);

        AccountDTO expectedAccountDTO = new AccountDTO();
        expectedAccountDTO.setCbuCvu(CBU);

        when(accountRepository.findByCbuCvu(Mockito.anyString())).thenReturn(Optional.empty());
        when(accountMapper.toEntity(Mockito.any(AccountRequestDTO.class))).thenReturn(accountEntity);
        when(accountRepository.save(Mockito.any(Account.class))).thenReturn(savedEntity);
        when(accountMapper.toDTO(Mockito.any(Account.class))).thenReturn(expectedAccountDTO);

        AccountDTO result = accountService.save(accountRequestDTO);

        assertNotNull(result);
        assertEquals(CBU, result.getCbuCvu());
    }

    @Test
    void testGetAll_returnsAccountDTOs() {
        Account account1 = new Account();
        account1.setCbuCvu(CBU);

        Account account2 = new Account();
        account2.setCbuCvu(CBU_2);

        AccountDTO accountDTO1 = new AccountDTO();
        accountDTO1.setCbuCvu(CBU);

        AccountDTO accountDTO2 = new AccountDTO();
        accountDTO2.setCbuCvu(CBU_2);

        when(accountRepository.findAll()).thenReturn(Arrays.asList(account1, account2));
        when(accountMapper.toAccountDTOs(Arrays.asList(account1, account2))).thenReturn(Arrays.asList(accountDTO1, accountDTO2));

        List<AccountDTO> result = accountService.getAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(CBU, result.get(0).getCbuCvu());
        assertEquals(CBU_2, result.get(1).getCbuCvu());
    }
}