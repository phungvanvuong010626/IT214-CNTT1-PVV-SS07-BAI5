package org.example.employeeservice.service.impl;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.example.employeeservice.dto.APIResponseDto;
import org.example.employeeservice.dto.DepartmentDto;
import org.example.employeeservice.dto.EmployeeDto;
import org.example.employeeservice.entity.Employee;
import org.example.employeeservice.exception.ResourceNotFoundException;
import org.example.employeeservice.openfeigns.DepartmentServiceCall;
import org.example.employeeservice.repository.EmployeeRepository;
import org.example.employeeservice.service.EmployeeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentServiceCall departmentServiceCall;

    @Override
    public EmployeeDto saveEmployee(EmployeeDto employeeDto) {
        // Validate department exists in department-service via OpenFeign
        DepartmentDto departmentDto = null;
        try {
            departmentDto = departmentServiceCall.getDepartmentById(employeeDto.getDepartmentId());
        } catch (FeignException.NotFound e) {
            throw new ResourceNotFoundException("Department", "id", employeeDto.getDepartmentId());
        } catch (Exception e) {
            throw new RuntimeException("Error communicating with department-service: " + e.getMessage());
        }

        if (departmentDto == null) {
            throw new ResourceNotFoundException("Department", "id", employeeDto.getDepartmentId());
        }

        Employee employee = new Employee(
                employeeDto.getId(),
                employeeDto.getFirstName(),
                employeeDto.getLastName(),
                employeeDto.getEmail(),
                employeeDto.getDepartmentId()
        );

        Employee savedEmployee = employeeRepository.save(employee);
        return mapToDto(savedEmployee);
    }

    @Override
    public APIResponseDto getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", id));

        DepartmentDto departmentDto = null;
        try {
            departmentDto = departmentServiceCall.getDepartmentById(employee.getDepartmentId());
        } catch (Exception e) {
            // Department service might be unavailable or department not found
            departmentDto = null;
        }

        EmployeeDto employeeDto = mapToDto(employee);
        return new APIResponseDto(employeeDto, departmentDto);
    }

    @Override
    public List<EmployeeDto> getAllEmployees() {
        return employeeRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public EmployeeDto updateEmployee(Long id, EmployeeDto employeeDto) {
        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", id));

        // Validate department exists in department-service via OpenFeign
        DepartmentDto departmentDto = null;
        try {
            departmentDto = departmentServiceCall.getDepartmentById(employeeDto.getDepartmentId());
        } catch (FeignException.NotFound e) {
            throw new ResourceNotFoundException("Department", "id", employeeDto.getDepartmentId());
        } catch (Exception e) {
            throw new RuntimeException("Error communicating with department-service: " + e.getMessage());
        }

        if (departmentDto == null) {
            throw new ResourceNotFoundException("Department", "id", employeeDto.getDepartmentId());
        }

        existingEmployee.setFirstName(employeeDto.getFirstName());
        existingEmployee.setLastName(employeeDto.getLastName());
        existingEmployee.setEmail(employeeDto.getEmail());
        existingEmployee.setDepartmentId(employeeDto.getDepartmentId());

        Employee updatedEmployee = employeeRepository.save(existingEmployee);
        return mapToDto(updatedEmployee);
    }

    @Override
    public void deleteEmployee(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", id));
        employeeRepository.delete(employee);
    }

    private EmployeeDto mapToDto(Employee employee) {
        return new EmployeeDto(
                employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmail(),
                employee.getDepartmentId()
        );
    }
}
