package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CustomerDao;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthenticationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import com.upgrad.FoodOrderingApp.service.exception.UpdateCustomerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.UUID;

@Service
public class CustomerService {

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private PasswordCryptographyProvider passwordCryptographyProvider;

    // customer validation
    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerEntity saveCustomer(CustomerEntity customerEntity) throws SignUpRestrictedException {


        // contact number validation
        if (customerDao.contactNumberExistsOrNot(customerEntity.getContactNumber()) != null) {
            throw new SignUpRestrictedException("SGR-001", "This contact number is already registered! Try other contact number.");
        }

        // email id format validation
        if (!customerEntity.getEmail().matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}")) {
            throw new SignUpRestrictedException("SGR-002", "Invalid email-id format!");
        }

        // contact number format validation
        if (!customerEntity.getContactNumber().matches("^[0][1-9]\\d{9}$|^[1-9]\\d{9}")) {
            throw new SignUpRestrictedException("SGR-003", "Invalid contact number!");
        }

        // password strength validation
        if (!customerEntity.getPassword().matches("^(?=.*?[A-Z])(?=.*?[0-9])(?=.*?[#@$%&*!^-]).{8,}$")) {
            throw new SignUpRestrictedException("SGR-004", "Weak password!");
        }

        // encrypt salt and password
        String[] encryptedText = passwordCryptographyProvider.encrypt(customerEntity.getPassword());
        customerEntity.setSalt(encryptedText[0]);
        customerEntity.setPassword(encryptedText[1]);

        return customerDao.createCustomer(customerEntity);
    }

    /* Method to login for valid users only or else throws an exception */
    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerAuthEntity authenticate(String username, String password) throws AuthenticationFailedException {

//      here username is the contact number of the user
        CustomerEntity customerEntity = customerDao.contactNumberExistsOrNot(username);

        if (customerEntity == null) {
            throw new AuthenticationFailedException("ATH-001", "This contact number has not been registered!");
        }

        final String encryptedPassword = PasswordCryptographyProvider.encrypt(password, customerEntity.getSalt());

        if (encryptedPassword.equals(customerEntity.getPassword())) {
            JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(encryptedPassword);
            CustomerAuthEntity customerAuthEntity = new CustomerAuthEntity();
            customerAuthEntity.setUuid(UUID.randomUUID().toString());
            customerAuthEntity.setCustomer(customerEntity);
            ZonedDateTime now = ZonedDateTime.now();
            ZonedDateTime expiresAt = now.plusHours(8);

            customerAuthEntity.setLoginAt(ZonedDateTime.now());
            customerAuthEntity.setExpiresAt(expiresAt);
            customerAuthEntity.setAccessToken(jwtTokenProvider.generateToken(customerEntity.getUuid(), now, expiresAt));

            return customerDao.createCustomerAuth(customerAuthEntity);
        } else {
            throw new AuthenticationFailedException("ATH-002", "Invalid Credentials");
        }
    }

    /* Method to logout customer based on access-token */
    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerAuthEntity logout(String accessToken) throws AuthorizationFailedException {

        CustomerAuthEntity customerAuthEntity = customerDao.getCustomerByAccessToken(accessToken);

        if (customerAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "Customer is not Logged in.");
        }

        if (customerAuthEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "Customer is logged out. Log in again to access this endpoint.");
        }

        ZonedDateTime now = ZonedDateTime.now();
        if (customerAuthEntity.getExpiresAt().isBefore(now)) {
            throw new AuthorizationFailedException("ATHR-003", "Your session is expired. Log in again to access this endpoint.");
        }

        customerAuthEntity.setLogoutAt(now);
        return customerDao.updateCustomerAuth(customerAuthEntity);
    }

    /*Get the Customer with details based on access-token */
    public CustomerEntity getCustomer(String accessToken) throws AuthorizationFailedException {
        CustomerAuthEntity customerAuthEntity = customerDao.getCustomerByAccessToken(accessToken);

        if (customerAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "Customer is not Logged in.");
        }

        if (customerAuthEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "Customer is logged out. Log in again to access this endpoint.");
        }

        ZonedDateTime now = ZonedDateTime.now();
        if (customerAuthEntity.getExpiresAt().isBefore(now)) {
            throw new AuthorizationFailedException("ATHR-003", "Your session is expired. Log in again to access this endpoint.");
        }

        return customerAuthEntity.getCustomer();
    }

    /* Update customer details*/
    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerEntity updateCustomer(CustomerEntity customerEntity) {
        return customerDao.updateCustomerEntity(customerEntity);
    }

    /* Update the customer password */
    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerEntity updateCustomerPassword(String oldPassword, String newPassword, CustomerEntity customerEntity) throws UpdateCustomerException {

        // validation for new password strength
        if (!newPassword.matches("^(?=.*?[A-Z])(?=.*?[0-9])(?=.*?[#@$%&*!^-]).{8,}$")) {
            throw new UpdateCustomerException("UCR-001", "Weak password!");
        }

        // validation for old password
        final String oldEncryptedPassword = PasswordCryptographyProvider.encrypt(oldPassword, customerEntity.getSalt());
        if (!oldEncryptedPassword.equals(customerEntity.getPassword())) {
            throw new UpdateCustomerException("UCR-004", "Incorrect old password!");
        }

        // encrypt salt and new password
        String[] encryptedText = passwordCryptographyProvider.encrypt(newPassword);
        customerEntity.setSalt(encryptedText[0]);
        customerEntity.setPassword(encryptedText[1]);

        return customerDao.updateCustomerEntity(customerEntity);
    }
}
