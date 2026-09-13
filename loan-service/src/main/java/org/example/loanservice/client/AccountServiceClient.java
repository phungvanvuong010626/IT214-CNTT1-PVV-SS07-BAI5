package org.example.loanservice.client;

import org.example.loanservice.dto.AccountDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "account-service")
public interface AccountServiceClient {

    @GetMapping("/api/accounts/customer/{customerId}")
    List<AccountDto> getAccountsByCustomerId(@PathVariable("customerId") Long customerId);
}
