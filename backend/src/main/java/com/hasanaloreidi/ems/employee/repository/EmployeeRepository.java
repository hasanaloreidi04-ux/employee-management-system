package com.hasanaloreidi.ems.employee.repository;

import com.hasanaloreidi.ems.employee.domain.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmployeeNumber(String employeeNumber);

    Optional<Employee> findByEmailIgnoreCase(String email);

    boolean existsByEmployeeNumber(String employeeNumber);

    boolean existsByEmailIgnoreCase(String email);
}