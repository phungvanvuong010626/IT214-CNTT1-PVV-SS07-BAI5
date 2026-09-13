package org.example.loanservice.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerDto {
    private Long id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String idCardNumber;
    private String address;
}
