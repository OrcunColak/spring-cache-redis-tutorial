package com.colak.springcacheredistutorial.employee.service;

import com.colak.springcacheredistutorial.employee.jpa.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {

    void save(Employee emp);

    List<Employee> findAll();

    Optional<Employee> findById(Long id);

    void deleteById(Long id);

    void deleteAll();
}
