package com.rohan.lms.employees.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rohan.lms.employees.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

	Employee findByEmployeeEmail(String email);
	
}
