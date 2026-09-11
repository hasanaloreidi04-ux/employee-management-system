package com.hasanaloreidi.ems.employee.exception;

public class DuplicateEmployeeException extends RuntimeException {

    public DuplicateEmployeeException(String field, String value) {
        super("Employee with " + field + " '" + value + "' already exists");
    }
}