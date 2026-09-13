package org.example.loanservice.service.impl;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.loanservice.client.AccountServiceClient;
import org.example.loanservice.client.CustomerServiceClient;
import org.example.loanservice.dto.AccountDto;
import org.example.loanservice.dto.CustomerDto;
import org.example.loanservice.dto.LoanApplyRequest;
import org.example.loanservice.dto.LoanResponseDto;
import org.example.loanservice.entity.Loan;
import org.example.loanservice.exception.BadRequestException;
import org.example.loanservice.exception.ResourceNotFoundException;
import org.example.loanservice.repository.LoanRepository;
import org.example.loanservice.service.LoanService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepository;
    private final CustomerServiceClient customerServiceClient;
    private final AccountServiceClient accountServiceClient;

    private static final BigDecimal ANNUAL_INTEREST_RATE = new BigDecimal("8.0"); // 8%/năm

    @Override
    public LoanResponseDto applyLoan(LoanApplyRequest request) {
        log.info("Bắt đầu xử lý đăng ký khoản vay cho customerId: {}", request.getCustomerId());

        // Bước 1: Gọi Customer Service qua Feign để lấy thông tin khách hàng (kiểm tra tồn tại, lấy tên, SĐT)
        CustomerDto customer;
        try {
            customer = customerServiceClient.getCustomerById(request.getCustomerId());
        } catch (FeignException.NotFound e) {
            throw new ResourceNotFoundException("Khách hàng không tồn tại với ID: " + request.getCustomerId());
        } catch (Exception e) {
            log.error("Lỗi khi gọi Customer Service: {}", e.getMessage());
            throw new RuntimeException("Không thể kết nối đến Customer Service: " + e.getMessage());
        }

        if (customer == null) {
            throw new ResourceNotFoundException("Khách hàng không tồn tại với ID: " + request.getCustomerId());
        }

        // Bước 2: Gọi Account Service qua Feign để lấy danh sách tài khoản (kiểm tra có tài khoản active không)
        List<AccountDto> accounts;
        try {
            accounts = accountServiceClient.getAccountsByCustomerId(request.getCustomerId());
        } catch (Exception e) {
            log.error("Lỗi khi gọi Account Service: {}", e.getMessage());
            throw new RuntimeException("Không thể kết nối đến Account Service: " + e.getMessage());
        }

        if (accounts == null || accounts.isEmpty()) {
            throw new BadRequestException("no active account: Khách hàng chưa có tài khoản ngân hàng");
        }

        List<AccountDto> activeAccounts = accounts.stream()
                .filter(acc -> "ACTIVE".equalsIgnoreCase(acc.getStatus()))
                .collect(Collectors.toList());

        if (activeAccounts.isEmpty()) {
            throw new BadRequestException("no active account: Khách hàng không có tài khoản nào ở trạng thái ACTIVE");
        }

        // Lấy tài khoản active đầu tiên làm tài khoản nhận giải ngân
        String disbursementAccount = activeAccounts.get(0).getAccountNumber();

        // Bước 3: Tính toán lãi suất dự kiến (8%/năm) và số tiền phải trả hàng tháng
        // Tổng tiền lãi = amount * (8 / 100) * (termMonths / 12)
        BigDecimal interestRateDecimal = ANNUAL_INTEREST_RATE.divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP);
        BigDecimal termYears = new BigDecimal(request.getTermMonths()).divide(new BigDecimal("12"), 4, RoundingMode.HALF_UP);
        BigDecimal totalInterest = request.getAmount().multiply(interestRateDecimal).multiply(termYears);
        BigDecimal totalRepayment = request.getAmount().add(totalInterest);

        // Số tiền phải trả hàng tháng = Tổng tiền trả / kỳ hạn
        BigDecimal monthlyPayment = totalRepayment.divide(new BigDecimal(request.getTermMonths()), 2, RoundingMode.HALF_UP);

        // Bước 4: Tạo bản ghi khoản vay với trạng thái PENDING
        Loan loan = Loan.builder()
                .customerId(customer.getId())
                .customerName(customer.getFullName())
                .customerPhone(customer.getPhoneNumber())
                .disbursementAccountNumber(disbursementAccount)
                .amount(request.getAmount())
                .termMonths(request.getTermMonths())
                .interestRate(ANNUAL_INTEREST_RATE)
                .monthlyPayment(monthlyPayment)
                .purpose(request.getPurpose())
                .status("PENDING")
                .createdAt(LocalDateTime.now())
                .build();

        Loan savedLoan = loanRepository.save(loan);
        log.info("Khoản vay được tạo thành công với ID: {}", savedLoan.getId());

        // Bước 5: Response trả về chi tiết khoản vay
        return mapToDto(savedLoan);
    }

    @Override
    public LoanResponseDto getLoanById(Long id) {
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy khoản vay với ID: " + id));
        return mapToDto(loan);
    }

    @Override
    public List<LoanResponseDto> getLoansByCustomerId(Long customerId) {
        return loanRepository.findByCustomerId(customerId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private LoanResponseDto mapToDto(Loan loan) {
        return LoanResponseDto.builder()
                .loanId(loan.getId())
                .customerId(loan.getCustomerId())
                .customerName(loan.getCustomerName())
                .customerPhone(loan.getCustomerPhone())
                .disbursementAccountNumber(loan.getDisbursementAccountNumber())
                .amount(loan.getAmount())
                .termMonths(loan.getTermMonths())
                .interestRate(loan.getInterestRate())
                .monthlyPayment(loan.getMonthlyPayment())
                .purpose(loan.getPurpose())
                .status(loan.getStatus())
                .createdAt(loan.getCreatedAt())
                .build();
    }
}
