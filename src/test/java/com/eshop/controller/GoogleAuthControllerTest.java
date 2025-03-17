package com.eshop.controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.eshop.repository.CustomerRepository;
import com.eshop.service.CustomUserDetailsService;
import com.eshop.utility.JWTUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@ExtendWith(MockitoExtension.class)
class GoogleAuthControllerTest {

    @InjectMocks
    private GoogleAuthController googleAuthController;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private CustomUserDetailsService userDetailsService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private JWTUtils jwtUtils;

    private MockMvc mockMvc;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(googleAuthController).build();
    }

    @Test
    void testHandleGoogleCallback_SuccessfulLogin() throws Exception {
        String code = "test_code";
        String email = "test@example.com";
        String jwtToken = "test_jwt_token";
        UserDetails userDetails = mock(UserDetails.class);

        when(restTemplate.postForEntity(anyString(), any(), eq(Map.class))).thenReturn(ResponseEntity.ok(Map.of("id_token", "test_id_token")));
        when(restTemplate.getForEntity(anyString(), eq(Map.class))).thenReturn(ResponseEntity.ok(Map.of("email", email)));
        when(userDetailsService.loadUserByUsername(email)).thenReturn(userDetails);
        when(jwtUtils.generateToken(userDetails)).thenReturn(jwtToken);

        ResponseEntity<?> response = googleAuthController.handleGoogleCallback(code);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(jwtToken, ((Map<?, ?>) response.getBody()).get("token"));
    }

    @Test
    void testHandleGoogleCallback_InvalidCode() throws Exception {
        String code = "invalid_code";

        when(restTemplate.postForEntity(anyString(), any(), eq(Map.class))).thenThrow(new RuntimeException("Invalid authorization code"));

        ResponseEntity<?> response = googleAuthController.handleGoogleCallback(code);
        assertEquals(500, response.getStatusCodeValue());
    }

    @Test
    void testHandleGoogleCallback_UnauthorizedUser() throws Exception {
        String code = "test_code";

        when(restTemplate.postForEntity(anyString(), any(), eq(Map.class))).thenReturn(ResponseEntity.ok(Map.of("id_token", "test_id_token")));
        when(restTemplate.getForEntity(anyString(), eq(Map.class))).thenReturn(ResponseEntity.status(401).build());

        ResponseEntity<?> response = googleAuthController.handleGoogleCallback(code);
        assertEquals(401, response.getStatusCodeValue());
    }
}
