package org.example.accountservice.service;

import org.example.accountservice.dto.AccountDto;

import java.util.List;

public interface AccountService {
    AccountDto createAccount(AccountDto accountDto);
    AccountDto getAccountById(Long id);
    List<AccountDto> getAccountsByCustomerId(Long customerId);
    List<AccountDto> getAllAccounts();
}
