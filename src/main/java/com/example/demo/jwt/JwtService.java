package com.example.demo.jwt;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtService {

	@Value("${jwt.salt}")
	private String salt;
	
	@Value("${jwt.expmin}")
	private Long expireMin;
	
	// token 생성 
	public String create(String userId, String password) {
		
		final JwtBuilder builder = Jwts.builder();
		builder.setHeaderParam("typ", "JWT");
		
		builder.setSubject("loginToken")
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * expireMin)) // 실제 사용시 1000 * 60 * expireMin
				.claim("userId", userId)
				.claim("password", password);
		
		builder.signWith(SignatureAlgorithm.HS256, salt.getBytes());
		
		final String jwtToken = builder.compact();
		
		return jwtToken;
	}
	
	// token 유효성 검사
		public String checkValid(String jwtToken) {
			
			Jwts.parser().setSigningKey(salt.getBytes()).parseClaimsJws(jwtToken);
			
			String userId = (String) this.get(jwtToken).get("userId");
			String password = (String) this.get(jwtToken).get("password");
			
			return this.create(userId, password);
			
		}
		
		// token에서 정보 추출
		public Map<String, Object> get(final String jwt) {
			
			Jws<Claims> claims = null; 
		
			try {
				claims = Jwts.parser().setSigningKey(salt.getBytes()).parseClaimsJws(jwt); 
			} catch (final Exception e) {
				throw new RuntimeException();
			}
			
			return claims.getBody();
			
		}
	
}
