package org.example.customerservice.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.customerservice.entity.Customer;
import org.example.customerservice.repository.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final CustomerRepository customerRepository;

    @Override
    public void run(String... args) {
        if (customerRepository.count() == 0) {
            Customer c1 = Customer.builder()
                    .fullName("Nguyễn Văn An")
                    .email("an.nguyen@gmail.com")
                    .phoneNumber("0901234567")
                    .idCardNumber("001200000001")
                    .address("Hà Nội")
                    .build();

            Customer c2 = Customer.builder()
                    .fullName("Trần Thị Bình")
                    .email("binh.tran@gmail.com")
                    .phoneNumber("0987654321")
                    .idCardNumber("001200000002")
                    .address("TP. Hồ Chí Minh")
                    .build();

            Customer c3 = Customer.builder()
                    .fullName("Lê Văn Cường")
                    .email("cuong.le@gmail.com")
                    .phoneNumber("0911223344")
                    .idCardNumber("001200000003")
                    .address("Đà Nẵng")
                    .build();

            customerRepository.save(c1);
            customerRepository.save(c2);
            customerRepository.save(c3);

            log.info("Initialized 3 sample customers (ID 1: Nguyễn Văn An, ID 2: Trần Thị Bình, ID 3: Lê Văn Cường)");
        }
    }
}
