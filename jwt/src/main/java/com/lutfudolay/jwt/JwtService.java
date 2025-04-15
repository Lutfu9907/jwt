package com.lutfudolay.jwt;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtService {

	public static final String SECRET_KEY = "0bIKTgffvlhfN+wZgvb7sOpNzGRh2iRGIckaFPjqbAI="; 

	public String generateToken(UserDetails userDetails) {  //"Bir kullanıcı sisteme login olduğunda, bu kullanıcı için bir JWT token üret."
		
		//Map<String, String> claimsMap = new HashMap<>();
		//claimsMap.put("role","ADMIN");
		
		return Jwts.builder() // Jwts sınıfının builder metodu kullanıldı
		.setSubject(userDetails.getUsername()) // Bu token kime ait? Genelde kullanıcı adı (username) konur.
		.setIssuedAt(new Date()) // Ne zaman üretildi?
		.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 2)) // Ne zaman geçerliliği biter? (şu an + 2 saat)
		.signWith(getKey(), SignatureAlgorithm.HS256) //Token’ı imzala (Key ve algoritma ile)
		.compact(); // Token’ı string haline getir ve döndür.  Dönen değer: Bir String token. Bu token kullanıcıya frontend’e gönderilir.

	}

	public <T> T exportToken(String token, Function<Claims, T> claimsFunction) { //"Verilen bir JWT token’ı çöz ve içerisindeki bilgileri oku"
		
		Claims claims = Jwts 
		.parserBuilder()		
		.setSigningKey(getKey())
		.build()
		.parseClaimsJws(token).getBody();
		
		return claimsFunction.apply(claims);
		
		//Token’ı alır, çözümlemek için aynı Key ile açar. 
		//Claims → Token’ın içeriğini temsil eder.
		//claimsFunction → Hangi bilgiyi istiyorsan ona göre getirir.
		////Bu yapınını amacı şu : Aynı method ile hem username'i alabiliriz, hem expiration süresini.
	}

	public String getUsernameByToken(String token) { //"Token içindeki kullanıcı adını getir" Subject = Kullanıcı adı (username)
		
		return exportToken(token, Claims::getSubject);
	}
	
	public boolean isTokenExpired(String token) { //"Token geçerli mi? Süresi dolmuş mu?"
		
		Date expiredDate = exportToken(token, Claims::getExpiration);
		
		return new Date().before(expiredDate);
	}

	public Key getKey() {
		byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
		
		return Keys.hmacShaKeyFor(keyBytes);
		
		//JWT oluştururken/verifier yaparken imzalama işlemi yapılır.
		//Bu Key aslında senin "imzan" gibidir.
		//SECRET_KEY gizli anahtardır, base64 ile encode edilmiştir.
		//Token’ların doğruluğunu kontrol ederken bu key’e ihtiyaç duyulur.
	}
}
