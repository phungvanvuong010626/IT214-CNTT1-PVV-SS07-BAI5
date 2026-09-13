package org.example.customerservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.customerservice.dto.CustomerDto;
import org.example.customerservice.entity.Customer;
import org.example.customerservice.repository.CustomerRepository;
import org.example.customerservice.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public CustomerDto createCustomer(CustomerDto customerDto) {
        Customer customer = Customer.builder()
                .fullName(customerDto.getFullName())
                .email(customerDto.getEmail())
                .phoneNumber(customerDto.getPhoneNumber())
                .idCardNumber(customerDto.getIdCardNumber())
                .address(customerDto.getAddress())
                .build();

        Customer saved = customerRepository.save(customer);
        return mapToDto(saved);
    }

    @Override
    public CustomerDto getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy khách hàng với ID: " + id));
        return mapToDto(customer);
    }

    @Override
    public List<CustomerDto> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private CustomerDto mapToDto(Customer customer) {
        return CustomerDto.builder()
                .id(customer.getId())
                .fullName(customer.getFullName())
                .email(customer.getEmail())
                .phoneNumber(customer.getPhoneNumber())
                .idCardNumber(customer.getIdCardNumber())
                .address(customer.getAddress())
                .build();
    }
}
