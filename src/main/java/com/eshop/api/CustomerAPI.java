package com.eshop.api;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private CustomerService customerService;
    @Autowired
    private Environment environment;
    @Autowired
    static Log logger = LogFactory.getLog(CustomerAPI.class);

    @PostMapping(value = "/login")
    public ResponseEntity<CustomerDTO> authenticateCustomer(@Valid @RequestBody CustomerCredDTO custCredDTO)
            throws EShopException {

        logger.info("CUSTOMER TRYING TO LOGIN, VALIDATING CREDENTIALS. CUSTOMER EMAIL ID: " + custCredDTO.getEmailId());
        CustomerDTO customerDTOFromDB = customerService.authenticateCustomer(custCredDTO.getEmailId(),
                custCredDTO.getPassword());
        logger.info("CUSTOMER LOGIN SUCCESS, CUSTOMER EMAIL : " + customerDTOFromDB.getEmailId());
        return new ResponseEntity<>(customerDTOFromDB, HttpStatus.OK);
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

}