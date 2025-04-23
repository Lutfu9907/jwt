package com.lutfudolay.service.impl;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lutfudolay.jwt.AuthResponse;
import com.lutfudolay.jwt.JwtService;
import com.lutfudolay.jwt.RefreshTokenRequest;
import com.lutfudolay.model.RefreshToken;
import com.lutfudolay.model.User;
import com.lutfudolay.repository.RefreshTokenRepository;
import com.lutfudolay.service.IRefreshTokenService;

@Service
public class RefreshTokenServiceImpl implements IRefreshTokenService {

	@Autowired
	private RefreshTokenRepository refreshTokenRepository;

	@Autowired
	private JwtService jwtService; // Yeni access token üretimi için gerekli olan servis.

	public boolean isRefreshTokenExpired(Date expiredDate) {
		return new Date().before(expiredDate);
	}

	private RefreshToken createRefreshToken(User user) { // yeni refresh token oluşturur.
		RefreshToken refreshToken = new RefreshToken();
		refreshToken.setRefreshToken(UUID.randomUUID().toString());
		refreshToken.setExpireDate(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 4));
		refreshToken.setUser(user);

		return refreshToken;
	}

	@Override
	public AuthResponse refreshToken(RefreshTokenRequest request) { // Frontend’den gelen refresh token isteklerini
																	// işleyen ana metot.

		Optional<RefreshToken> optional = refreshTokenRepository.findByRefreshToken(request.getRefreshToken()); //Veritabanında gelen refresh token aranıyor.

		if (optional.isEmpty()) {
			System.out.println("refresh token geçersiz" + request.getRefreshToken());
		}

		RefreshToken refreshToken = optional.get(); // Eğer token süresi geçmişse → Kullanıcıya token üretme izni verilmez.
		if (!isRefreshTokenExpired(refreshToken.getExpireDate())) {
			System.out.println("refresh token expire olmuştur" + request.getRefreshToken());
		}

		String accessToken = jwtService.generateToken(refreshToken.getUser());
		RefreshToken newRefreshToken = createRefreshToken(refreshToken.getUser());// Aynı kullanıcı için yeni bir refresh token daha oluşturulur.
		RefreshToken savedRefreshToken = refreshTokenRepository.save(createRefreshToken(refreshToken.getUser()));

		return new AuthResponse(accessToken, savedRefreshToken.getRefreshToken());
	}
}