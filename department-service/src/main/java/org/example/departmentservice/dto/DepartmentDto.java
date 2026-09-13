package org.example.departmentservice.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentDto {
    private Long id;

    @NotEmpty(message = "Tên phòng ban không được để trống")
    private String departmentName;

    private String departmentDescription;

    @NotEmpty(message = "Mã phòng ban không được để trống")
    private String departmentCode;
}
