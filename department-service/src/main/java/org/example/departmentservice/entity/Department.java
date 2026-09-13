package org.example.departmentservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "departments")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "department_name")
    private String departmentName;

    @Column(name = "department_description")
    private String departmentDescription;

    @Column(nullable = false, unique = true, name = "department_code")
    private String departmentCode;
}
