package com.hasanaloreidi.ems.employee.exception;

public class EmployeeNotFoundException extends RuntimeException {

    public EmployeeNotFoundException(Long employeeId) {
        super("Employee with id " + employeeId + " was not found");
    }
}