package com.colak.springcacheredistutorial.employee.repository;

import com.colak.springcacheredistutorial.employee.jpa.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
