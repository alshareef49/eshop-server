package com.eshop.controller;


import com.eshop.dto.AuthResponse;
import com.eshop.dto.CustomerCredDTO;
import com.eshop.dto.CustomerDTO;
import com.eshop.exception.EShopException;
import com.eshop.service.CustomUserDetailsService;
import com.eshop.service.CustomerService;
import com.eshop.utility.JWTUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerAPITest {

    @InjectMocks
    private CustomerAPI customerAPI;

    @Mock
    private JWTUtils jwtUtils;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private CustomUserDetailsService userDetailsService;

    @Mock
    private CustomerService customerService;

    @Mock
    private Environment environment;

    private CustomerCredDTO customerCredDTO;
    private CustomerDTO customerDTO;

    @BeforeEach
    void setUp() {
        customerCredDTO = new CustomerCredDTO();
        customerCredDTO.setEmailId("test@example.com");
        customerCredDTO.setPassword("password123");

        customerDTO = new CustomerDTO();
        customerDTO.setEmailId("test@example.com");
    }

    @Test
    void authenticateCustomer_ValidCredentials_ReturnsAuthResponse() throws EShopException {
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetailsService.loadUserByUsername(any())).thenReturn(userDetails);
        when(jwtUtils.generateToken(any())).thenReturn("mocked-token");

        ResponseEntity<?> response = customerAPI.authenticateCustomer(customerCredDTO);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof AuthResponse);
        assertEquals("mocked-token", ((AuthResponse) response.getBody()).getToken());
    }

    @Test
    void authenticateCustomer_InvalidCredentials_ReturnsUnauthorized() throws EShopException {
        doThrow(new BadCredentialsException("Invalid credentials"))
                .when(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));

        ResponseEntity<?> response = customerAPI.authenticateCustomer(customerCredDTO);

        assertNotNull(response);
        assertEquals(401, response.getStatusCodeValue());
        assertEquals("Invalid credentials", response.getBody());
    }

    @Test
    void registerCustomer_Success_ReturnsSuccessMessage() throws EShopException {
        when(customerService.registerNewCustomer(any())).thenReturn("test@example.com");
        when(environment.getProperty("CustomerAPI.CUSTOMER_REGISTRATION_SUCCESS")).thenReturn("Registered Successfully: ");

        ResponseEntity<String> response = customerAPI.registerCustomer(customerDTO);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Registered Successfully: test@example.com", response.getBody());
    }

    @Test
    void updateShippingAddress_Success_ReturnsSuccessMessage() throws EShopException {
        when(environment.getProperty("CustomerAPI.UPDATE_ADDRESS_SUCCESS")).thenReturn("Address Updated Successfully");

        ResponseEntity<String> response = customerAPI.updateShippingAddress("test@example.com", "New Address");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Address Updated Successfully", response.getBody());
    }

    @Test
    void deleteShippingAddress_Success_ReturnsSuccessMessage() throws EShopException {
        when(environment.getProperty("CustomerAPI.CUSTOMER_ADDRESS_DELETED_SUCCESS")).thenReturn("Address Deleted Successfully");

        ResponseEntity<String> response = customerAPI.deleteShippingAddress("test@example.com");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Address Deleted Successfully", response.getBody());
    }

    @Test
    void updatePassword_Success_ReturnsSuccessMessage() throws EShopException {
        when(environment.getProperty("CustomerAPI.UPDATE_PASSWORD_SUCCESS")).thenReturn("Password Updated Successfully");

        ResponseEntity<String> response = customerAPI.updatePassword("test@example.com", "newPassword");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Password Updated Successfully", response.getBody());
    }

    @Test
    void updatePhoneNumber_Success_ReturnsSuccessMessage() throws EShopException {
        when(environment.getProperty("CustomerAPI.UPDATE_PHONE_NUMBER_SUCCESS")).thenReturn("Phone Number Updated Successfully");

        ResponseEntity<String> response = customerAPI.updatePhoneNumber("test@example.com", "9876543210");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Phone Number Updated Successfully", response.getBody());
    }
}