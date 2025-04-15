package com.lutfudolay.controller;

import com.lutfudolay.dto.DtoEmployee;

public interface IRestEmployeeController {

	public DtoEmployee findEmployeeById(Long id);
}
