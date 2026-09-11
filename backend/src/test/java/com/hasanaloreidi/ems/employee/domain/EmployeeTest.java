package com.hasanaloreidi.ems.employee.domain;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class EmployeeTest {

    @Test
    void shouldSetDefaultStatusAndTimestampsBeforePersisting() {
        Employee employee = createEmployee(null);

        assertNull(employee.getStatus());
        assertNull(employee.getCreatedAt());
        assertNull(employee.getUpdatedAt());

        employee.onCreate();

        assertEquals(EmployeeStatus.ACTIVE, employee.getStatus());
        assertNotNull(employee.getCreatedAt());
        assertNotNull(employee.getUpdatedAt());
        assertEquals(employee.getCreatedAt(), employee.getUpdatedAt());
    }

    @Test
    void shouldUpdateTimestampBeforeUpdating() {
        Employee employee = createEmployee(EmployeeStatus.ACTIVE);
        employee.onCreate();
        Instant previousUpdatedAt = employee.getUpdatedAt();

        employee.onUpdate();

        assertNotNull(employee.getUpdatedAt());
        assertFalse(employee.getUpdatedAt().isBefore(previousUpdatedAt));
    }

    private Employee createEmployee(EmployeeStatus status) {
        return new Employee(
                "EMP-001",
                "Hasan",
                "Aloreidi",
                "hasan@example.com",
                "+49 123 456789",
                "Software Developer",
                "Engineering",
                LocalDate.of(2026, 9, 11),
                status);
    }
}