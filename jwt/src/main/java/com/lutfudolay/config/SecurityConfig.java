package com.lutfudolay.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.lutfudolay.jwt.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity //Spring Security’yi aktif hale getirir

public class SecurityConfig {//Uygulamanın hangi endpoint'lerine kim ulaşabilir, nasıl giriş yapılır, token nasıl kontrol edilir gibi güvenlik politikalarını burada tanımlarız.

	public static final  String AUTHENTICATE = "/authenticate"; //Public erişime açık olacak endpoint’leri sabit olarak tanımlıyorsun.
	public static final  String REGISTER = "/register";
	
	@Autowired
	private AuthenticationProvider authenticationProvider;
	
	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
		
		//SecurityFilterChain Spring Security’nin tüm filtre zincirini tanımlar.
		//Burada istekler nasıl kontrol edilecek, güvenlik nasıl uygulanacak hepsi bu metotta belirlenir.
		
		http.csrf().disable() //CSRF (Cross-Site Request Forgery) saldırılarına karşı koruma.
		.authorizeHttpRequests(request->
		 request.requestMatchers(AUTHENTICATE,REGISTER)
		.permitAll() //"/authenticate" ve "/register" adreslerine herkes erişebilir (permitAll()).
		.anyRequest()
		.authenticated()) // Diğer tüm istekler JWT token ile doğrulanmalıdır (authenticated()).
		.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		//STATELESS = Sunucu tarafında oturum tutulmaz.
		//Çünkü biz JWT kullanıyoruz. Yani her istek kendi içinde token taşıyor.
		//Spring, hiçbir kullanıcı bilgisini hafızasında tutmaz.
		.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); 
		//jwtAuthenticationFilter filtresi, UsernamePasswordAuthenticationFilter'dan önce çalışacak.
		//Yani önce token kontrolü yapılır, sonra login kontrolleri devreye girer.
		
		return http.build();
	}
}
