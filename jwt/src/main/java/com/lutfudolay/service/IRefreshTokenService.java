package com.lutfudolay.service;

import com.lutfudolay.jwt.AuthResponse;
import com.lutfudolay.jwt.RefreshTokenRequest;

public interface IRefreshTokenService {

	AuthResponse refreshToken(RefreshTokenRequest request);
}
