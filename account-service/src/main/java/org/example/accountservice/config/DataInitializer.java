package org.example.accountservice.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.accountservice.entity.Account;
import org.example.accountservice.repository.AccountRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final AccountRepository accountRepository;

    @Override
    public void run(String... args) {
        if (accountRepository.count() == 0) {
            // Khách hàng 1: 2 tài khoản ACTIVE
            Account a1 = Account.builder()
                    .accountNumber("101000123456")
                    .customerId(1L)
                    .balance(new BigDecimal("50000000"))
                    .accountType("CHECKING")
                    .status("ACTIVE")
                    .build();

            Account a2 = Account.builder()
                    .accountNumber("101000987654")
                    .customerId(1L)
                    .balance(new BigDecimal("200000000"))
                    .accountType("SAVINGS")
                    .status("ACTIVE")
                    .build();

            // Khách hàng 2: 1 tài khoản ACTIVE
            Account a3 = Account.builder()
                    .accountNumber("202000112233")
                    .customerId(2L)
                    .balance(new BigDecimal("35000000"))
                    .accountType("CHECKING")
                    .status("ACTIVE")
                    .build();

            // Khách hàng 4 (nếu có): tài khoản INACTIVE để test case status
            Account a4 = Account.builder()
                    .accountNumber("404000999888")
                    .customerId(4L)
                    .balance(BigDecimal.ZERO)
                    .accountType("CHECKING")
                    .status("INACTIVE")
                    .build();

            accountRepository.save(a1);
            accountRepository.save(a2);
            accountRepository.save(a3);
            accountRepository.save(a4);

            log.info("Initialized sample accounts for customer 1 (2 accounts), customer 2 (1 account), customer 4 (inactive account)");
        }
    }
}
