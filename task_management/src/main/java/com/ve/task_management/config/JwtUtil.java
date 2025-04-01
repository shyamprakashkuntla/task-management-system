package com.ve.task_management.config;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtUtil {
	
	@Value("${jwt.expiration}")
	private long jwtExpiration;
	
	//we are not using this because this is not a secure one and gets error in console.
	private String secretKey = "";
	//at this point we are not assigning because this has to be assigned in the below
	//class constructor implementation
	
	/*
	private final SecretKey secretKey;
	
	public JwtUtil(SecretKey secretKey) {
		this.secretKey = secretKey;
	}
	 */
	//private static final long jwt_expiration = 1*24*60*60*1000;
	

	public JwtUtil() {
		try {
			KeyGenerator generator = KeyGenerator.getInstance("HmacSHA256");
			SecretKey sk = generator.generateKey();
			secretKey = Base64.getEncoder().encodeToString(sk.getEncoded());
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		
	}

	public String generateToken(String username)
	{
		Map<String, Object> claims = new HashMap<>();
		
		return Jwts.builder()
				.claims()
				.add(claims)
				.subject(username)
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis()+jwtExpiration))
				.and()
				.signWith(getKey())
				.compact();
		
	}

	private Key getKey() {
		byte[] keybyte = Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor(keybyte);
	}

	
	//below is for validating token jwt 
	
	
	private Claims extractAllClaims(String token)
	{
		return Jwts.parser()
				.verifyWith((SecretKey) getKey())
				.build()
				.parseSignedClaims(token)
				.getPayload();
	}
	
	
	private <T> T extractclaim(String token,Function<Claims, T> claimResolver)
	{
		final Claims claims = extractAllClaims(token);
		return claimResolver.apply(claims);
	}
	
	
	
	public String extractUsername(String token) 
	{
		// extract the username for jwt token
		return extractclaim(token, Claims::getSubject);
	}
	
	public Date extractExpiration(String token)
	{
		return extractclaim(token, Claims::getExpiration);
	}
	
	public Date extractCreatedAt(String token)
	{
		return extractclaim(token, Claims::getIssuedAt);
	}
	
	private boolean isTokenexpired(String token)
	{
		return extractExpiration(token).before(new Date());
	}

	public boolean validatetoken(String token, UserDetails userDetails) 
	{
		final String userName = extractUsername(token);
		return (userName.equals(userDetails.getUsername()) && !isTokenexpired(token)) ;
	}
}
