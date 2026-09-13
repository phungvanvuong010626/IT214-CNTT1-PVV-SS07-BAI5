package org.example.loanservice.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanApplyRequest {

    @NotNull(message = "customerId không được để trống")
    private Long customerId;

    @NotNull(message = "Số tiền vay không được để trống")
    @DecimalMin(value = "1000000", message = "Số tiền vay tối thiểu là 1,000,000 VND")
    private BigDecimal amount;

    @NotNull(message = "Kỳ hạn vay không được để trống")
    @Min(value = 1, message = "Kỳ hạn vay tối thiểu là 1 tháng")
    private Integer termMonths;

    @NotBlank(message = "Mục đích vay không được để trống")
    private String purpose;
}
