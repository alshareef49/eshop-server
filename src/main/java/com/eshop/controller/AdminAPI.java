package com.eshop.controller;


import com.eshop.dto.CustomerDTO;
import com.eshop.exception.EShopException;
import com.eshop.models.Customer;
import com.eshop.service.CustomUserDetailsService;
import com.eshop.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminAPI {

    @Autowired
    private CustomerService customerService;


    @GetMapping("/all-users")
    public ResponseEntity<?> getAllUsers() throws EShopException {
        List<Customer> all = customerService.getAllCustomers();
        if (all != null && !all.isEmpty()) {
            return new ResponseEntity<>(all, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/create-admin-user")
    public ResponseEntity<?> createUser(@RequestBody CustomerDTO customerDTO) throws EShopException {
        customerService.saveAdmin(customerDTO);
        String message = "Admin created successfully";
        return new ResponseEntity<>(message,HttpStatus.CREATED);
    }

}
