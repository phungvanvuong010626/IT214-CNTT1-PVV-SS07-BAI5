package org.example.employeeservice.openfeigns;

import org.example.employeeservice.dto.DepartmentDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "department-service")
public interface DepartmentServiceCall {

    @GetMapping("/{id}")
    DepartmentDto getDepartmentById(@PathVariable("id") Long id);

    @GetMapping
    List<DepartmentDto> getAllDepartments();

    @GetMapping("/{id}/exists")
    Boolean checkDepartmentExists(@PathVariable("id") Long id);
}
