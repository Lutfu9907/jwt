package com.lutfudolay.controller;

import com.lutfudolay.dto.DtoUser;
import com.lutfudolay.jwt.AuthRequest;
import com.lutfudolay.jwt.AuthResponse;

public interface IRestAuthController{

	public DtoUser register (AuthRequest request);
	
	public AuthResponse authenticate(AuthRequest request) ;
}
