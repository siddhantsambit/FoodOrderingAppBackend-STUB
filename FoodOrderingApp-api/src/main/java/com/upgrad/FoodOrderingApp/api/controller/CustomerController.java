package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.business.CustomerService;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthenticationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import com.upgrad.FoodOrderingApp.service.exception.UpdateCustomerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    /**
     * This api endpoint is used to signup/register a new customer in Food Ordering App
     *
     * @param signupCustomerRequest this argument contains all the attributes required to create a new customer in the database
     *
     * @return ResponseEntity<SignupCustomerResponse> type object along with HttpStatus CREATED
     *
     * @throws SignUpRestrictedException if contact number provided already exists in the current database
     * @throws SignUpRestrictedException if any field other than last name is empty
     * @throws SignUpRestrictedException If the email ID provided by the customer is not in the correct format
     * @throws SignUpRestrictedException If the contact number provided by the customer is not in correct format
     * @throws SignUpRestrictedException If the password provided by the customer is weak
     */

    @CrossOrigin
    @RequestMapping(method = RequestMethod.POST, path = "/customer/signup", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SignupCustomerResponse> signup(
            @RequestBody(required = false) final SignupCustomerRequest signupCustomerRequest)
            throws SignUpRestrictedException
    {
        // Validation for required fields
        if (
                signupCustomerRequest.getFirstName().equals("") ||
                        signupCustomerRequest.getEmailAddress().equals("") ||
                        signupCustomerRequest.getContactNumber().equals("") ||
                        signupCustomerRequest.getPassword().equals("")
        ) {
            throw new SignUpRestrictedException("SGR-005", "Except last name all fields should be filled");
        }

        final CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setUuid(UUID.randomUUID().toString());
        customerEntity.setFirstName(signupCustomerRequest.getFirstName());
        customerEntity.setLastName(signupCustomerRequest.getLastName());
        customerEntity.setEmail(signupCustomerRequest.getEmailAddress());
        customerEntity.setContactNumber(signupCustomerRequest.getContactNumber());
        customerEntity.setPassoword(signupCustomerRequest.getPassword());

        final CustomerEntity createdCustomerEntity = customerService.saveCustomer(customerEntity);
        SignupCustomerResponse customerResponse = new SignupCustomerResponse()
                .id(createdCustomerEntity.getUuid()).status("CUSTOMER SUCCESSFULLY REGISTERED");
        return new ResponseEntity<SignupCustomerResponse>(customerResponse, HttpStatus.CREATED);
    }


    /**
     * This api endpoint is used to login/SignIn an existing customer
     *
     * @param authorization customer credentials in 'Basic Base64<contactNumber:password>' format
     * @return ResponseEntity<LoginResponse> type object along with HttpStatus OK
     *"JwtAccessToken” class has been used to generate an access token
     *returns same the access token in in the Response Header
     *
     * @throws AuthenticationFailedException If the Basic authentication is not provided incorrect format
     * @throws AuthenticationFailedException If the contact number provided by the customer does not exist
     * @throws AuthenticationFailedException If the password provided by the customer does not match the password in the existing database
     * @throws AuthenticationFailedException If the Basic authentication is not provided incorrect format
     */

    @CrossOrigin
    @RequestMapping(method = RequestMethod.POST, path = "/customer/login", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<LoginResponse> login(
            @RequestHeader("authorization") final String authorization)
            throws AuthenticationFailedException
    {
        byte[] decode;
        String contactNumber;
        String customerPassword;
        try {
            decode = Base64.getDecoder().decode(authorization.split("Basic ")[1]);
            String decodedText = new String(decode);
            String[] decodedArray = decodedText.split(":");
            contactNumber = decodedArray[0];
            customerPassword = decodedArray[1];
        } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException ex) {
            throw new AuthenticationFailedException("ATH-003", "Incorrect format of decoded customer name and password");
        }

        CustomerAuthEntity createdCustomerAuthEntity = customerService.authenticate(contactNumber, customerPassword);

        LoginResponse loginResponse = new LoginResponse()
                .id(createdCustomerAuthEntity.getCustomer()
                        .getUuid()).message("LOGGED IN SUCCESSFULLY");

        loginResponse.setId(createdCustomerAuthEntity.getCustomer().getUuid());
        loginResponse.setFirstName(createdCustomerAuthEntity.getCustomer().getFirstName());
        loginResponse.setLastName(createdCustomerAuthEntity.getCustomer().getLastName());
        loginResponse.setContactNumber(createdCustomerAuthEntity.getCustomer().getContactNumber());
        loginResponse.setEmailAddress(createdCustomerAuthEntity.getCustomer().getEmail());

        HttpHeaders headers = new HttpHeaders();
        headers.add("access-token", createdCustomerAuthEntity.getAccessToken());
        List<String> header = new ArrayList<>();
        header.add("access-token");
        headers.setAccessControlExposeHeaders(header);

        return new ResponseEntity<LoginResponse>(loginResponse, headers, HttpStatus.OK);
    }

    /**
     * This api endpoint is used to logout an existing customer
     *
     * @param authorization customer login access token in 'Bearer <access-token>' format
     *
     * @return ResponseEntity<LogoutResponse> type object along with HttpStatus OK
     *
     * @throws AuthorizationFailedException If the access token provided by the customer does not exist in the database
     * @throws AuthorizationFailedException If the access token provided by the customer exists in the database
     * @throws AuthorizationFailedException If the access token provided by the customer exists in the database, but the customer has already logged out
     * @throws AuthorizationFailedException If the access token provided by the customer exists in the database, but the session has expired
     */

    @CrossOrigin
    @RequestMapping(method = RequestMethod.POST, path = "/customer/logout", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<LogoutResponse> logout(
            @RequestHeader("authorization") final String authorization)
            throws AuthorizationFailedException
    {
        String accessToken = authorization.split("Bearer ")[1];
        CustomerAuthEntity customerAuthEntity = customerService.logout(accessToken);
        LogoutResponse logoutResponse = new LogoutResponse()
                .id(customerAuthEntity.getCustomer().getUuid()).message("LOGGED OUT SUCCESSFULLY");
        return new ResponseEntity<LogoutResponse>(logoutResponse, HttpStatus.OK);
    }


    /**
     * This api endpoint is used to update an existing customer
     *
     * @param updateCustomerRequest this argument contains all the attributes required to update a customer in the database
     * @param authorization customer access token in 'Bearer <access-token>' format
     *
     * @return ResponseEntity<UpdateCustomerResponse> type object along with HttpStatus OK
     *
     * @throws UpdateCustomerException If firstname field is empty
     * @throws AuthorizationFailedException if  the access token provided by the customer does not exist in the database
     * @throws AuthorizationFailedException if validation on customer access token failsf the access token provided by the customer does not exist in the database
     * @throws AuthorizationFailedException If the access token provided by the customer exists in the database, but the customer has already logged out
     * @throws AuthorizationFailedException If the access token provided by the customer exists in the database, but the session has expired
     */

    @CrossOrigin
    @RequestMapping(method = RequestMethod.PUT, path = "/customer", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<UpdateCustomerResponse> update(
            @RequestBody(required = false) final UpdateCustomerRequest updateCustomerRequest,
            @RequestHeader("authorization") final String authorization)
            throws AuthorizationFailedException, UpdateCustomerException
    {
        if (updateCustomerRequest.getFirstName().equals("")) {
            throw new UpdateCustomerException("UCR-002", "First name field should not be empty");
        }

        String accessToken = authorization.split("Bearer ")[1];
        CustomerEntity customerEntity = customerService.getCustomer(accessToken);

        customerEntity.setFirstName(updateCustomerRequest.getFirstName());
        if (!updateCustomerRequest.getLastName().equals("")) {
            customerEntity.setLastName(updateCustomerRequest.getLastName());
        }

        CustomerEntity updatedCustomerEntity = customerService.updateCustomer(customerEntity);
        UpdateCustomerResponse customerResponse = new UpdateCustomerResponse()
                .id(updatedCustomerEntity.getUuid()).status("CUSTOMER DETAILS UPDATED SUCCESSFULLY");
        customerResponse.setFirstName(updatedCustomerEntity.getFirstName());
        customerResponse.setLastName(updatedCustomerEntity.getLastName());
        return new ResponseEntity<UpdateCustomerResponse>(customerResponse, HttpStatus.OK);
    }

    /**
     *
     * This api endpoint is used to update password
     *
     * @param updatePasswordRequest this argument contains all the attributes required to update a customer's password in the database
     * @param authorization customer access token in 'Bearer <access-token>' format
     *
     * @return ResponseEntity<UpdatePasswordResponse> type object along with HttpStatus OK
     *
     * @throws AuthorizationFailedException If the access token provided by the customer does not exist in the database
     * @throws AuthorizationFailedException f the new password provided by the customer is weak
     * @throws AuthorizationFailedException If the access token provided by the customer exists in the database, but the session has expired
     * @throws AuthorizationFailedException If the access token provided by the customer exists in the database, but the customer has already logged out
     * @throws UpdateCustomerException If the old or new password field is empty
     * @throws UpdateCustomerException If the old password field entered is incorrect
     */

    @CrossOrigin
    @RequestMapping(method = RequestMethod.PUT, path = "/customer/password", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<UpdatePasswordResponse> changePassword(
            @RequestBody(required = false) final UpdatePasswordRequest updatePasswordRequest,
            @RequestHeader("authorization") final String authorization)
            throws AuthorizationFailedException, UpdateCustomerException
    {
        if (updatePasswordRequest.getOldPassword().equals("") || updatePasswordRequest.getNewPassword().equals("")) {
            throw new UpdateCustomerException("UCR-003", "No field should be empty");
        }

        String accessToken = authorization.split("Bearer ")[1];
        CustomerEntity customerEntity = customerService.getCustomer(accessToken);

        CustomerEntity updatedCustomerEntity = customerService.updateCustomerPassword(
                updatePasswordRequest.getOldPassword(),
                updatePasswordRequest.getNewPassword(),
                customerEntity
        );

        UpdatePasswordResponse updatePasswordResponse = new UpdatePasswordResponse()
                .id(updatedCustomerEntity.getUuid())
                .status("CUSTOMER PASSWORD UPDATED SUCCESSFULLY");
        return new ResponseEntity<UpdatePasswordResponse>(updatePasswordResponse, HttpStatus.OK);
    }
}
