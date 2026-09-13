package org.example.customerservice.service;

import org.example.customerservice.dto.CustomerDto;

import java.util.List;

public interface CustomerService {
    CustomerDto createCustomer(CustomerDto customerDto);
    CustomerDto getCustomerById(Long id);
    List<CustomerDto> getAllCustomers();
}
