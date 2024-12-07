package com.katabank.mapper;

import com.katabank.dto.AccountDTO;
import com.katabank.dto.AccountRequestDTO;
import com.katabank.entity.Account;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper
public interface AccountMapper {
    Account toEntity (AccountDTO accountDTO);
    Account toEntity (AccountRequestDTO accountDTO);
    AccountDTO toDTO (Account account);
    List<Account> toAccountEntities(List<AccountDTO> accountDTOs);
    List<AccountDTO> toAccountDTOs(List<Account> accounts);

}
