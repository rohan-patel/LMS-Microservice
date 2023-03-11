package com.rohan.lms.gateway.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rohan.lms.gateway.config.JwtConfig;
import com.rohan.lms.gateway.exception.JwtTokenIncorrectStructureException;
import com.rohan.lms.gateway.exception.JwtTokenMalformedException;
import com.rohan.lms.gateway.exception.JwtTokenMissingException;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenUtil {
	
	@Autowired
	private JwtConfig config;
	
	public String generateToken(String email) {
		SecretKey key = Keys.hmacShaKeyFor(config.getSecret().getBytes(StandardCharsets.UTF_8));
		
		String jwt = Jwts.builder()
				.setIssuer("Rohan Patel")
				.setSubject("JWT token")
				.claim("username", email)
				.setIssuedAt(new Date())
				.setExpiration(new Date((new Date()).getTime() + config.getValidity()*1000*60))
				.signWith(key)
				.compact();
		
		return jwt;
	}
	
	public void validateToken(final String header) throws JwtTokenMalformedException, JwtTokenMissingException {
		try {
			
			SecretKey key = Keys.hmacShaKeyFor(
					config.getSecret().getBytes(StandardCharsets.UTF_8));
			
			String[] parts = header.split(" ");
			if (parts.length != 2 || !"Bearer".equals(parts[0])) {
				throw new JwtTokenIncorrectStructureException("Incorrect Authentication Structure");
			}

			Jwts.parser().setSigningKey(key).parseClaimsJws(parts[1]);
		} catch (SignatureException ex) {
			throw new JwtTokenMalformedException("Invalid JWT signature");
		} catch (MalformedJwtException ex) {
			throw new JwtTokenMalformedException("Invalid JWT token");
		} catch (ExpiredJwtException ex) {
			throw new JwtTokenMalformedException("Expired JWT token");
		} catch (UnsupportedJwtException ex) {
			throw new JwtTokenMalformedException("Unsupported JWT token");
		} catch (IllegalArgumentException ex) {
			throw new JwtTokenMissingException("JWT claims string is empty.");
		}
	}

}
