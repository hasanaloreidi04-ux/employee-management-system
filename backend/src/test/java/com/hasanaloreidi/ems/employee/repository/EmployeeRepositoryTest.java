package com.hasanaloreidi.ems.employee.repository;

import com.hasanaloreidi.ems.employee.domain.Employee;
import com.hasanaloreidi.ems.employee.domain.EmployeeStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    void shouldSaveAndFindEmployeeByEmployeeNumber() {
        Employee savedEmployee = employeeRepository.saveAndFlush(createEmployee());

        Optional<Employee> result = employeeRepository.findByEmployeeNumber("EMP-001");

        assertTrue(result.isPresent());
        assertNotNull(savedEmployee.getId());
        assertEquals(savedEmployee.getId(), result.get().getId());
        assertEquals("Hasan", result.get().getFirstName());
    }

    @Test
    void shouldFindEmployeeByEmailIgnoringCase() {
        employeeRepository.saveAndFlush(createEmployee());

        Optional<Employee> result = employeeRepository.findByEmailIgnoreCase("HASAN@EXAMPLE.COM");

        assertTrue(result.isPresent());
        assertEquals("hasan@example.com", result.get().getEmail());
    }

    private Employee createEmployee() {
        return new Employee(
                "EMP-001",
                "Hasan",
                "Aloreidi",
                "hasan@example.com",
                "+49 123 456789",
                "Software Developer",
                "Engineering",
                LocalDate.of(2026, 9, 11),
                EmployeeStatus.ACTIVE);
    }
}