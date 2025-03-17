package com.eshop.controller;

import com.eshop.dto.AuthResponse;
import com.eshop.service.CustomUserDetailsService;
import com.eshop.utility.JWTUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.eshop.dto.CustomerCredDTO;
import com.eshop.dto.CustomerDTO;
import com.eshop.exception.EShopException;
import com.eshop.service.CustomerService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;


@RestController
@RequestMapping(value = "/customer-api")
public class CustomerAPI {
    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private CustomUserDetailsService userDetailsService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private Environment environment;
    @Autowired
    static Log logger = LogFactory.getLog(CustomerAPI.class);

    @PostMapping(value = "/login")
    public ResponseEntity<?> authenticateCustomer(@Valid @RequestBody CustomerCredDTO custCredDTO)
            throws EShopException {

        logger.info("CUSTOMER TRYING TO LOGIN, VALIDATING CREDENTIALS. CUSTOMER EMAIL ID: " + custCredDTO.getEmailId());
        try {
            this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(custCredDTO.getEmailId(), custCredDTO.getPassword()));
        } catch (BadCredentialsException var4) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }

        UserDetails userDetails = this.userDetailsService.loadUserByUsername(custCredDTO.getEmailId());
        String token = this.jwtUtils.generateToken(userDetails);
        logger.info("CUSTOMER LOGIN SUCCESS, CUSTOMER EMAIL : " + custCredDTO.getEmailId());
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping(value = "/register")
    public ResponseEntity<String> registerCustomer(@Valid @RequestBody CustomerDTO customerDTO) throws EShopException {

        logger.info("CUSTOMER TRYING TO REGISTER. CUSTOMER EMAIL ID: " + customerDTO.getEmailId());
        String registeredWithEmailID = customerService.registerNewCustomer(customerDTO);
        registeredWithEmailID = environment.getProperty("CustomerAPI.CUSTOMER_REGISTRATION_SUCCESS")
                + registeredWithEmailID;
        return new ResponseEntity<>(registeredWithEmailID, HttpStatus.OK);
    }

    @PutMapping(value = "/customer/{customerEmailId:.+}/address/")
    public ResponseEntity<String> updateShippingAddress(
            @PathVariable @Pattern(regexp = "[a-zA-Z0-9._]+@[a-zA-Z]{2,}\\.[a-zA-Z][a-zA-Z.]+", message = "{invalid.email.format}") String customerEmailId,
            @RequestBody String address) throws EShopException {

        customerService.updateShippingAddress(customerEmailId, address);
        String modificationSuccessMsg = environment.getProperty("CustomerAPI.UPDATE_ADDRESS_SUCCESS");
        return new ResponseEntity<>(modificationSuccessMsg, HttpStatus.OK);

    }

    @DeleteMapping(value = "/customer/{customerEmailId:.+}")
    public ResponseEntity<String> deleteShippingAddress(
            @Pattern(regexp = "[a-zA-Z0-9._]+@[a-zA-Z]{2,}\\.[a-zA-Z][a-zA-Z.]+", message = "{invalid.email.format}") @PathVariable("customerEmailId") String customerEmailId)
            throws EShopException {

        customerService.deleteShippingAddress(customerEmailId);
        String modificationSuccessMsg = environment.getProperty("CustomerAPI.CUSTOMER_ADDRESS_DELETED_SUCCESS");
        return new ResponseEntity<>(modificationSuccessMsg, HttpStatus.OK);

    }

    @PutMapping(value = "/customer/{customerEmailId:.+}/password/")
    public ResponseEntity<String> updatePassword(
            @PathVariable @Pattern(regexp = "[a-zA-Z0-9._]+@[a-zA-Z]{2,}\\.[a-zA-Z][a-zA-Z.]+", message = "{invalid.email.format}") String customerEmailId,
            @RequestBody String newPassword) throws EShopException {

            customerService.updatePassword(customerEmailId, newPassword);
            String modificationSuccessMsg = environment.getProperty("CustomerAPI.UPDATE_PASSWORD_SUCCESS");
            return new ResponseEntity<>(modificationSuccessMsg, HttpStatus.OK);
    }

    @PutMapping(value = "/customer/{customerEmailId:.+}")
    public ResponseEntity<String> updatePhoneNumber(
            @PathVariable @Pattern(regexp = "[a-zA-Z0-9._]+@[a-zA-Z]{2,}\\.[a-zA-Z][a-zA-Z.]+", message = "{invalid.email.format}") String customerEmailId,
            @RequestParam("phoneNumber") String phoneNumber) throws EShopException {

            customerService.updatePhoneNumber(customerEmailId, phoneNumber);
            String modificationSuccessMsg = environment.getProperty("CustomerAPI.UPDATE_PHONE_NUMBER_SUCCESS");
            return new ResponseEntity<>(modificationSuccessMsg, HttpStatus.OK);
            }
}