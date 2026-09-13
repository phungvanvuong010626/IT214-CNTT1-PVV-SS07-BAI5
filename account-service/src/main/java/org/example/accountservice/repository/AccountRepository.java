package org.example.accountservice.repository;

import org.example.accountservice.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByCustomerId(Long customerId);
    List<Account> findByCustomerIdAndStatus(Long customerId, String status);
    Optional<Account> findByAccountNumber(String accountNumber);
}
