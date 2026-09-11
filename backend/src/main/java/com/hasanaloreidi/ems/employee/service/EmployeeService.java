package com.hasanaloreidi.ems.employee.service;

import com.hasanaloreidi.ems.employee.domain.Employee;
import com.hasanaloreidi.ems.employee.exception.DuplicateEmployeeException;
import com.hasanaloreidi.ems.employee.exception.EmployeeNotFoundException;
import com.hasanaloreidi.ems.employee.repository.EmployeeRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

@Service
@Transactional(readOnly = true)
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> findAll() {
        return employeeRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    public Employee findById(Long employeeId) {
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException(employeeId));
    }

    @Transactional
    public Employee create(Employee employee) {
        normalize(employee);
        validateUniqueFields(employee, null);

        return employeeRepository.save(employee);
    }

    @Transactional
    public Employee update(Long employeeId, Employee updatedEmployee) {
        Employee existingEmployee = findById(employeeId);

        normalize(updatedEmployee);
        validateUniqueFields(updatedEmployee, employeeId);

        existingEmployee.setEmployeeNumber(updatedEmployee.getEmployeeNumber());
        existingEmployee.setFirstName(updatedEmployee.getFirstName());
        existingEmployee.setLastName(updatedEmployee.getLastName());
        existingEmployee.setEmail(updatedEmployee.getEmail());
        existingEmployee.setPhoneNumber(updatedEmployee.getPhoneNumber());
        existingEmployee.setJobTitle(updatedEmployee.getJobTitle());
        existingEmployee.setDepartment(updatedEmployee.getDepartment());
        existingEmployee.setHireDate(updatedEmployee.getHireDate());
        existingEmployee.setStatus(updatedEmployee.getStatus());

        return employeeRepository.save(existingEmployee);
    }

    @Transactional
    public void delete(Long employeeId) {
        Employee employee = findById(employeeId);
        employeeRepository.delete(employee);
    }

    private void validateUniqueFields(Employee employee, Long currentEmployeeId) {
        employeeRepository.findByEmployeeNumber(employee.getEmployeeNumber())
                .filter(existing -> !existing.getId().equals(currentEmployeeId))
                .ifPresent(existing -> {
                    throw new DuplicateEmployeeException(
                            "employee number",
                            employee.getEmployeeNumber());
                });

        employeeRepository.findByEmailIgnoreCase(employee.getEmail())
                .filter(existing -> !existing.getId().equals(currentEmployeeId))
                .ifPresent(existing -> {
                    throw new DuplicateEmployeeException(
                            "email",
                            employee.getEmail());
                });
    }

    private void normalize(Employee employee) {
        employee.setEmployeeNumber(employee.getEmployeeNumber().trim());
        employee.setFirstName(employee.getFirstName().trim());
        employee.setLastName(employee.getLastName().trim());
        employee.setEmail(
                employee.getEmail().trim().toLowerCase(Locale.ROOT));
        employee.setPhoneNumber(normalizeOptional(employee.getPhoneNumber()));
        employee.setJobTitle(employee.getJobTitle().trim());
        employee.setDepartment(employee.getDepartment().trim());
    }

    private String normalizeOptional(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return value.trim();
    }
}