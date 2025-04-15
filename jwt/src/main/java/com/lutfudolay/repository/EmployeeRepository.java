package com.lutfudolay.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lutfudolay.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long>{

}
