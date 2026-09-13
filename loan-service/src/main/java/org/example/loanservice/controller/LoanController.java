package org.example.loanservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.loanservice.dto.LoanApplyRequest;
import org.example.loanservice.dto.LoanResponseDto;
import org.example.loanservice.service.LoanService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;

    // Yêu cầu 3: POST /api/loans/apply
    @PostMapping("/apply")
    public ResponseEntity<LoanResponseDto> applyLoan(@Valid @RequestBody LoanApplyRequest request) {
        LoanResponseDto response = loanService.applyLoan(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoanResponseDto> getLoanById(@PathVariable("id") Long id) {
        LoanResponseDto loan = loanService.getLoanById(id);
        return ResponseEntity.ok(loan);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<LoanResponseDto>> getLoansByCustomerId(@PathVariable("customerId") Long customerId) {
        List<LoanResponseDto> loans = loanService.getLoansByCustomerId(customerId);
        return ResponseEntity.ok(loans);
    }
}
