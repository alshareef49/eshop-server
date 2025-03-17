package com.eshop.config;

import com.eshop.service.CustomUserDetailsService;
import com.eshop.utility.JWTUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class AuthenticationFilterConfigTest {

    @Mock
    private CustomUserDetailsService userDetailsService;

    @Mock
    private JWTUtils jwtUtils;

    @Mock
    private AuthCodeConfig authCodeConfig;

    @InjectMocks
    private AuthenticationFilterConfig authenticationFilterConfig;

    @Mock
    private FilterChain filterChain;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private UserDetails userDetails;

    @Test
    void shouldAuthenticateUser_WhenValidTokenProvided() throws ServletException, IOException {
        // Arrange
        String jwtToken = "valid.jwt.token";
        String email = "test@example.com";
        String authHeader = "Bearer " + jwtToken;

        when(request.getHeader("Authorization")).thenReturn(authHeader);
        when(jwtUtils.getUsernameFromToken(jwtToken)).thenReturn(email);
        when(userDetailsService.loadUserByUsername(email)).thenReturn(userDetails);
        when(jwtUtils.validateToken(jwtToken, userDetails)).thenReturn(true);

        // Act
        authenticationFilterConfig.doFilterInternal(request, response, filterChain);

        // Assert
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void shouldNotAuthenticateUser_WhenAuthorizationHeaderIsMissing() throws ServletException, IOException {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn(null);

        // Act
        authenticationFilterConfig.doFilterInternal(request, response, filterChain);

        // Assert
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void shouldNotAuthenticateUser_WhenInvalidTokenProvided() throws ServletException, IOException {
        // Arrange
        String jwtToken = "invalid.jwt.token";
        String email = "test@example.com";
        String authHeader = "Bearer " + jwtToken;

        when(request.getHeader("Authorization")).thenReturn(authHeader);
        when(jwtUtils.getUsernameFromToken(jwtToken)).thenReturn(email);
        when(userDetailsService.loadUserByUsername(email)).thenReturn(userDetails);
        when(jwtUtils.validateToken(jwtToken, userDetails)).thenReturn(false);

        // Act
        authenticationFilterConfig.doFilterInternal(request, response, filterChain);

        // Assert
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void shouldNotAuthenticateUser_WhenTokenIsExpired() throws ServletException, IOException {
        // Arrange
        String jwtToken = "expired.jwt.token";
        String email = "test@example.com";
        String authHeader = "Bearer " + jwtToken;

        when(request.getHeader("Authorization")).thenReturn(authHeader);
        when(jwtUtils.getUsernameFromToken(jwtToken)).thenReturn(email);
        when(userDetailsService.loadUserByUsername(email)).thenReturn(userDetails);
        when(jwtUtils.validateToken(jwtToken, userDetails)).thenReturn(false);

        // Act
        authenticationFilterConfig.doFilterInternal(request, response, filterChain);

        // Assert
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void shouldNotAuthenticateUser_WhenTokenHasNoBearerPrefix() throws ServletException, IOException {
        // Arrange
        String authHeader = "InvalidTokenWithoutBearerPrefix";

        when(request.getHeader("Authorization")).thenReturn(authHeader);

        // Act
        authenticationFilterConfig.doFilterInternal(request, response, filterChain);

        // Assert
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain, times(1)).doFilter(request, response);
    }


}