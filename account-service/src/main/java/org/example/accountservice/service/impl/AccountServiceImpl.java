package org.example.accountservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.accountservice.dto.AccountDto;
import org.example.accountservice.entity.Account;
import org.example.accountservice.repository.AccountRepository;
import org.example.accountservice.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Override
    public AccountDto createAccount(AccountDto accountDto) {
        Account account = Account.builder()
                .accountNumber(accountDto.getAccountNumber())
                .customerId(accountDto.getCustomerId())
                .balance(accountDto.getBalance())
                .accountType(accountDto.getAccountType() != null ? accountDto.getAccountType() : "CHECKING")
                .status(accountDto.getStatus() != null ? accountDto.getStatus() : "ACTIVE")
                .build();

        Account saved = accountRepository.save(account);
        return mapToDto(saved);
    }

    @Override
    public AccountDto getAccountById(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy tài khoản với ID: " + id));
        return mapToDto(account);
    }

    @Override
    public List<AccountDto> getAccountsByCustomerId(Long customerId) {
        return accountRepository.findByCustomerId(customerId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AccountDto> getAllAccounts() {
        return accountRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private AccountDto mapToDto(Account account) {
        return AccountDto.builder()
                .id(account.getId())
                .accountNumber(account.getAccountNumber())
                .customerId(account.getCustomerId())
                .balance(account.getBalance())
                .accountType(account.getAccountType())
                .status(account.getStatus())
                .build();
    }
}
