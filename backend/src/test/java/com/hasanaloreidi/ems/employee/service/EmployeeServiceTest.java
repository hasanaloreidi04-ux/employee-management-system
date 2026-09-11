package com.hasanaloreidi.ems.employee.service;

import com.hasanaloreidi.ems.employee.domain.Employee;
import com.hasanaloreidi.ems.employee.domain.EmployeeStatus;
import com.hasanaloreidi.ems.employee.exception.DuplicateEmployeeException;
import com.hasanaloreidi.ems.employee.exception.EmployeeNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class EmployeeServiceTest {

    @Autowired
    private EmployeeService employeeService;

    @Test
    void shouldCreateAndNormalizeEmployee() {
        Employee employee = new Employee(
                " EMP-001 ",
                " Hasan ",
                " Aloreidi ",
                " HASAN@EXAMPLE.COM ",
                "   ",
                " Software Developer ",
                " Engineering ",
                LocalDate.of(2026, 9, 11),
                null);

        Employee savedEmployee = employeeService.create(employee);

        assertNotNull(savedEmployee.getId());
        assertEquals("EMP-001", savedEmployee.getEmployeeNumber());
        assertEquals("Hasan", savedEmployee.getFirstName());
        assertEquals("Aloreidi", savedEmployee.getLastName());
        assertEquals("hasan@example.com", savedEmployee.getEmail());
        assertNull(savedEmployee.getPhoneNumber());
        assertEquals("Software Developer", savedEmployee.getJobTitle());
        assertEquals("Engineering", savedEmployee.getDepartment());
        assertEquals(EmployeeStatus.ACTIVE, savedEmployee.getStatus());
    }

    @Test
    void shouldRejectDuplicateEmailIgnoringCase() {
        employeeService.create(
                createEmployee("EMP-001", "hasan@example.com"));

        Employee duplicateEmployee = createEmployee("EMP-002", "HASAN@EXAMPLE.COM");

        assertThrows(
                DuplicateEmployeeException.class,
                () -> employeeService.create(duplicateEmployee));
    }

    @Test
    void shouldThrowExceptionWhenEmployeeDoesNotExist() {
        assertThrows(
                EmployeeNotFoundException.class,
                () -> employeeService.findById(Long.MAX_VALUE));
    }

    @Test
    void shouldUpdateExistingEmployee() {
        Employee savedEmployee = employeeService.create(
                createEmployee("EMP-001", "hasan@example.com"));

        Employee updatedEmployee = new Employee(
                "EMP-001",
                "Hasan",
                "Aloreidi",
                "hasan@example.com",
                "+49 987 654321",
                "Senior Software Developer",
                "Platform Engineering",
                LocalDate.of(2026, 9, 11),
                EmployeeStatus.ON_LEAVE);

        Employee result = employeeService.update(
                savedEmployee.getId(),
                updatedEmployee);

        assertEquals(
                "Senior Software Developer",
                result.getJobTitle());
        assertEquals(
                "Platform Engineering",
                result.getDepartment());
        assertEquals(EmployeeStatus.ON_LEAVE, result.getStatus());
    }

    @Test
    void shouldDeleteExistingEmployee() {
        Employee savedEmployee = employeeService.create(
                createEmployee("EMP-001", "hasan@example.com"));

        employeeService.delete(savedEmployee.getId());

        assertThrows(
                EmployeeNotFoundException.class,
                () -> employeeService.findById(savedEmployee.getId()));
    }

    private Employee createEmployee(
            String employeeNumber,
            String email) {
        return new Employee(
                employeeNumber,
                "Hasan",
                "Aloreidi",
                email,
                "+49 123 456789",
                "Software Developer",
                "Engineering",
                LocalDate.of(2026, 9, 11),
                EmployeeStatus.ACTIVE);
    }
}