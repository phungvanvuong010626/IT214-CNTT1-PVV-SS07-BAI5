package org.example.departmentservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.departmentservice.dto.DepartmentDto;
import org.example.departmentservice.entity.Department;
import org.example.departmentservice.exception.ResourceNotFoundException;
import org.example.departmentservice.repository.DepartmentRepository;
import org.example.departmentservice.service.DepartmentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Override
    public DepartmentDto createDepartment(DepartmentDto departmentDto) {
        Department department = new Department(
                departmentDto.getId(),
                departmentDto.getDepartmentName(),
                departmentDto.getDepartmentDescription(),
                departmentDto.getDepartmentCode()
        );
        Department savedDepartment = departmentRepository.save(department);
        return mapToDto(savedDepartment);
    }

    @Override
    public DepartmentDto getDepartmentById(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", id));
        return mapToDto(department);
    }

    @Override
    public List<DepartmentDto> getAllDepartments() {
        return departmentRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public DepartmentDto updateDepartment(Long id, DepartmentDto departmentDto) {
        Department existingDepartment = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", id));

        existingDepartment.setDepartmentName(departmentDto.getDepartmentName());
        existingDepartment.setDepartmentDescription(departmentDto.getDepartmentDescription());
        existingDepartment.setDepartmentCode(departmentDto.getDepartmentCode());

        Department updatedDepartment = departmentRepository.save(existingDepartment);
        return mapToDto(updatedDepartment);
    }

    @Override
    public void deleteDepartment(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", id));
        departmentRepository.delete(department);
    }

    @Override
    public boolean existsById(Long id) {
        return departmentRepository.existsById(id);
    }

    private DepartmentDto mapToDto(Department department) {
        return new DepartmentDto(
                department.getId(),
                department.getDepartmentName(),
                department.getDepartmentDescription(),
                department.getDepartmentCode()
        );
    }
}
