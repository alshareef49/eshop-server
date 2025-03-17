package com.eshop.utility;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.security.core.userdetails.UserDetails;

class JWTUtilsTest {

    @InjectMocks
    private JWTUtils jwtUtils;

    @Mock
    private UserDetails userDetails;

    private String secretKey = "testsecretkeytestsecretkeytestsecretkey";
    private long expireTokenTime = 3600; // 1 hour

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(jwtUtils, "secret", secretKey);
        ReflectionTestUtils.setField(jwtUtils, "expireTokenTime", expireTokenTime);
    }

    @Test
    void shouldGenerateValidToken() {
        when(userDetails.getUsername()).thenReturn("testuser");

        String token = jwtUtils.generateToken(userDetails);

        assertNotNull(token);
        assertTrue(token.length() > 10);
    }

    @Test
    void shouldExtractUsernameFromToken() {

        String username = "testuser";
        String token = generateTestToken(username);
        String extractedUsername = jwtUtils.getUsernameFromToken(token);
        assertEquals(username, extractedUsername);
    }

    @Test
    void shouldExtractExpirationDateFromToken() {
        String username = "testuser";
        String token = generateTestToken(username);

        Date expirationDate = jwtUtils.getExpirationDateFromToken(token);
        assertNotNull(expirationDate);
        assertTrue(expirationDate.after(new Date()));
    }

    @Test
    void shouldValidateTokenForMatchingUser() {
        String username = "testuser";
        String token = generateTestToken(username);
        when(userDetails.getUsername()).thenReturn(username);
        boolean isValid = jwtUtils.validateToken(token, userDetails);
        assertTrue(isValid);
    }

    @Test
    void shouldReturnFalseForExpiredToken() {

        String expiredToken = generateExpiredTestToken("expireduser");
        assertThrows(ExpiredJwtException.class, () -> jwtUtils.validateToken(expiredToken, userDetails));

    }

    private String generateTestToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireTokenTime * 1000))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private String generateExpiredTestToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis() - (expireTokenTime * 2000)))
                .setExpiration(new Date(System.currentTimeMillis() - (expireTokenTime * 1000)))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSigningKey() {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
