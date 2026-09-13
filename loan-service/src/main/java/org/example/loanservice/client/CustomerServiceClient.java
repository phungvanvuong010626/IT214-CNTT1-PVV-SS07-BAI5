package org.example.loanservice.client;

import org.example.loanservice.dto.CustomerDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "customer-service")
public interface CustomerServiceClient {

    @GetMapping("/api/customers/{id}")
    CustomerDto getCustomerById(@PathVariable("id") Long id);
}
