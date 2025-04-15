package com.lutfudolay.service.impl;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lutfudolay.dto.DtoDepartment;
import com.lutfudolay.dto.DtoEmployee;
import com.lutfudolay.model.Department;
import com.lutfudolay.model.Employee;
import com.lutfudolay.repository.EmployeeRepository;
import com.lutfudolay.service.IEmployeeService;

@Service
public class EmployeeServiceImpl implements IEmployeeService{

	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Override
	public DtoEmployee findEmployeeById(Long id) {
		
		DtoEmployee dtoEmployee = new DtoEmployee();
		DtoDepartment dtoDepartment = new DtoDepartment(); 
		
		Optional<Employee> optional = employeeRepository.findById(id);
		if(optional.isEmpty()) {
			return null;
		}
		
		Employee employee= optional.get();
		Department department = employee.getDepartment();
		
		BeanUtils.copyProperties(employee, dtoEmployee);
		BeanUtils.copyProperties(department, dtoDepartment);
		
		dtoEmployee.setDepartment(dtoDepartment);
		return dtoEmployee;
	}
}
