package org.example.loanservice.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountDto {
    private Long id;
    private String accountNumber;
    private Long customerId;
    private BigDecimal balance;
    private String accountType;
    private String status; // ACTIVE, INACTIVE
}
