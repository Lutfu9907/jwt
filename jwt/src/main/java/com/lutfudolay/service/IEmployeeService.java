package com.lutfudolay.service;

import com.lutfudolay.dto.DtoEmployee;

public interface IEmployeeService {

	DtoEmployee findEmployeeById(Long id);
}
