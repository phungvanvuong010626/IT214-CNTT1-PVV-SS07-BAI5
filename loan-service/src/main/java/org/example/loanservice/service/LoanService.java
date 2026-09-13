package org.example.loanservice.service;

import org.example.loanservice.dto.LoanApplyRequest;
import org.example.loanservice.dto.LoanResponseDto;

import java.util.List;

public interface LoanService {
    LoanResponseDto applyLoan(LoanApplyRequest request);
    LoanResponseDto getLoanById(Long id);
    List<LoanResponseDto> getLoansByCustomerId(Long customerId);
}
