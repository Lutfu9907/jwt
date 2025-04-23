package com.lutfudolay.config;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.lutfudolay.model.User;
import com.lutfudolay.repository.UserRepository;

@Configuration
public class AppConfig { // Spring Security'de authentication (kimlik doğrulama) ile ilgili yapı taşlarını oluşturmak için kullanılan bir konfigürasyon sınıfıdır.

	@Autowired
	private UserRepository userRepository;

	@Bean
	public UserDetailsService userDetailsService() { //Bu metod, JWT içinde gelen username’e göre veritabanından kullanıcıyı bulmak için kullanılır.
		return new UserDetailsService() { //UserDetailsService bir Spring interface’idir.
			//loadUserByUsername() metodunu override ederek, senin kendi kullanıcı sorgunu yazmanı sağlar.

			@Override
			public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
				Optional<User> optional = userRepository.findByUsername(username);
				if (optional.isPresent()) {
					return optional.get();
				}
				return null;
			}
		};
	}

	@Bean
	public AuthenticationProvider authenticationProvider() { //Bu yapı, kullanıcı doğrulamasını yapacak olan asıl sağlayıcıdır (provider).

		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(); //DaoAuthenticationProvider => Kullanıcı adı var mı?Şifresi doğru mu?
		authenticationProvider.setUserDetailsService(userDetailsService());//UserDetailsService ile kullanıcıyı yükler,
		authenticationProvider.setPasswordEncoder(passwordEncoder());//PasswordEncoder ile şifreyi kontrol eder.

		return authenticationProvider;
	}

	@Bean
	public AuthenticationManager auhtAuthenticationManager(AuthenticationConfiguration configuration) throws Exception {//Bu yapı, authentication işlemini yöneten merkezdir.
		return configuration.getAuthenticationManager(); //Controller’da kullanıcı login olduğunda bu yapı devreye girer.
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() { //Şifreleri hashlemek ve doğrulamak için kullanılır.
		return new BCryptPasswordEncoder(); // Her hash sonucu farklıdır, bu yüzden aynı şifre tekrar yazılsa bile hash sonucu aynı olmaz.
	}
}
