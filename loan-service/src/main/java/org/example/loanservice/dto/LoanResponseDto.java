package org.example.loanservice.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanResponseDto {
    private Long loanId;
    private Long customerId;
    private String customerName;
    private String customerPhone;
    private String disbursementAccountNumber;
    private BigDecimal amount;
    private Integer termMonths;
    private BigDecimal interestRate; // Ví dụ 8.0 (%/năm)
    private BigDecimal monthlyPayment; // Số tiền phải trả hàng tháng
    private String purpose;
    private String status; // PENDING
    private LocalDateTime createdAt;
}
