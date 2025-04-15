package com.lutfudolay.controller.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.lutfudolay.controller.IRestAuthController;
import com.lutfudolay.dto.DtoUser;
import com.lutfudolay.jwt.AuthRequest;
import com.lutfudolay.jwt.AuthResponse;
import com.lutfudolay.service.IAuthService;

import jakarta.validation.Valid;

public class RestAuthControllerImpl implements IRestAuthController{

	@Autowired
	private IAuthService authService;
	
	@PostMapping("/register")
	@Override
	public DtoUser register(@Valid @RequestBody AuthRequest request) {
		
		return authService.register(request);
	}

	@PostMapping("/authenticate")
	@Override
	public AuthResponse authenticate(@Valid @RequestBody AuthRequest request) {
		
		return authService.authenticate(request);
	}

	
}
