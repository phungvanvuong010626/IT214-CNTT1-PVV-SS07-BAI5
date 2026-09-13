package org.example.accountservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountDto {
    private Long id;

    @NotBlank(message = "Số tài khoản không được để trống")
    private String accountNumber;

    @NotNull(message = "ID khách hàng không được để trống")
    private Long customerId;

    @NotNull(message = "Số dư không được để trống")
    private BigDecimal balance;

    @NotBlank(message = "Loại tài khoản không được để trống")
    private String accountType;

    @NotBlank(message = "Trạng thái không được để trống")
    private String status; // ACTIVE, INACTIVE
}
