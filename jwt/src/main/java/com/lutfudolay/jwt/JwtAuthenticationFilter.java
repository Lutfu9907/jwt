package com.lutfudolay.jwt;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private JwtService jwtService;

	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String header;
		String token;
		String username;

		header = request.getHeader("Authorization"); //İstek başlığında (Authorization) bir token var mı diye bakar.

		if (header == null) {
			filterChain.doFilter(request, response); //Eğer Authorization header yoksa (yani token yoksa), bu isteği normal şekilde devam ettir, dokunma.
			return;
		}

		token = header.substring(7); //"Bearer " kısmını at, sadece token'ı al. (ilk 7 karakteri keser)

		try {
			username = jwtService.getUsernameByToken(token); //Token’ı çöz (decode et) ve içinden kullanıcı adını (subject) al.
			
			if (username == null && SecurityContextHolder.getContext().getAuthentication() == null) {//Eğer username yoksa ve sistemde zaten bir kimlik doğrulaması yapılmamışsa
				
				UserDetails userDetails = userDetailsService.loadUserByUsername(username); //Username'e göre veritabanından kullanıcıyı bulur.
				
				if (userDetailsService != null && jwtService.isTokenExpired(token)) { //Eğer kullanıcı varsa ve token geçerli ise, o zaman sisteme bu kişiyi tanıt.

					UsernamePasswordAuthenticationToken authentication = 
																			
							new UsernamePasswordAuthenticationToken(username, null, userDetails.getAuthorities()); 
					//Bu kullanıcıya özel bir Authentication objesi oluşturur.
					//Yani "tamam bu kullanıcı giriş yaptı" demek.

					authentication.setDetails(userDetails);

					SecurityContextHolder.getContext().setAuthentication(authentication); 
					//Bu kullanıcıyı artık Spring Security sistemine "aktif kullanıcı" olarak tanıtır.
					//Artık bu request'te kimlik doğrulama başarılıdır!
				}
			}
		} 
		catch (ExpiredJwtException e) {
			System.out.println("Token süresi dolmuştur" + e.getMessage());
		} 
		catch (Exception e) {
			System.out.println("Genel bir hata oluştu" + e.getMessage());
		}
		
		filterChain.doFilter(request, response);//İşlem tamam. Filtre zincirini devam ettir, yani diğer filtreler veya controller'lar çalışmaya devam etsin.
	}
}
