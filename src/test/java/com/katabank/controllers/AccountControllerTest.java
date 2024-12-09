package com.katabank.controllers;

import com.katabank.dto.AccountDTO;
import com.katabank.dto.AccountRequestDTO;
import com.katabank.services.AccountService;
import com.katabank.services.MovementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.security.auth.login.AccountNotFoundException;
import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@SpringBootTest
class AccountControllerTest {

    private MockMvc mockMvc;
    @Mock
    private AccountService accountService;
    @Mock
    private MovementService movementService;
    @InjectMocks
    private AccountController accountController;
    public static final String CBU = "2850590940090418135201";

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(accountController).build();
    }

    @Test
    void shouldCreateAccount_whenValidDataIsProvided() throws Exception {
        var accountRequestDTO = new AccountRequestDTO();
        accountRequestDTO.setCbuCvu(CBU);
        accountRequestDTO.setBalance(BigDecimal.valueOf(1000));

        var accountDTO = new AccountDTO();
        accountDTO.setCbuCvu(CBU);
        accountDTO.setBalance(BigDecimal.valueOf(1000));

        when(accountService.save(accountRequestDTO)).thenReturn(accountDTO);

        mockMvc.perform(post("/banking/v1/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"cbuCvu\": \"2850590940090418135201\", \"balance\": 1000 }"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.cbuCvu").value("2850590940090418135201"))
                .andExpect(jsonPath("$.balance").value(1000));

        verify(accountService, times(1)).save(accountRequestDTO);
    }

    @Test
    void shouldReturnListOfAccounts_whenAccountsExist() throws Exception {
        var accountDTO = new AccountDTO();
        accountDTO.setCbuCvu(CBU);
        accountDTO.setBalance(BigDecimal.valueOf(1000));

        when(accountService.getAll()).thenReturn(List.of(accountDTO));

        mockMvc.perform(get("/banking/v1/accounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].cbuCvu").value("2850590940090418135201"))
                .andExpect(jsonPath("$[0].balance").value(1000));

        verify(accountService, times(1)).getAll();
    }

    @Test
    void shouldReturnAccountMovements_whenValidAccountIdAndDateRangeAreProvided() throws Exception {
        mockMvc.perform(get("/banking/v1/accounts/{account}/movements", "2850590940090418135201")
                        .param("startDate", "2024-12-01T00:00:00")
                        .param("endDate", "2024-12-05T23:59:59"))
                .andExpect(status().isOk());

        verify(movementService, times(1)).getMovements(eq("2850590940090418135201"), any(), any());
    }

    @Test
    void shouldReturnBadRequest_whenRequestBodyIsEmpty() throws Exception {
        mockMvc.perform(post("/banking/v1/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());

        verify(accountService, times(0)).save(any());
    }

}