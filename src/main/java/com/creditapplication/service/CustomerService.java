package com.creditapplication.service;

import com.creditapplication.domain.Customer;
import com.creditapplication.domain.Role;
import com.creditapplication.domain.enums.RoleType;
import com.creditapplication.dto.CustomerDTO;
import com.creditapplication.dto.mapper.CustomerMapper;
import com.creditapplication.dto.request.AdminCustomerUpdateRequest;
import com.creditapplication.dto.request.CustomerUpdateRequest;
import com.creditapplication.dto.request.RegisterRequest;
import com.creditapplication.dto.request.UpdatePasswordRequest;
import com.creditapplication.exception.BadRequestException;
import com.creditapplication.exception.ConflictException;
import com.creditapplication.exception.ResourceNotFoundException;
import com.creditapplication.exception.message.ErrorMessage;
import com.creditapplication.repository.CustomerRepository;
import com.creditapplication.repository.RoleRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@AllArgsConstructor
public class CustomerService {

    private CustomerRepository customerRepository;

    private RoleRepository roleRepository;

    private PasswordEncoder passwordEncoder;

    private CustomerMapper customerMapper;

    public void register(RegisterRequest registerRequest) {
        if (customerRepository.existsByEmail(registerRequest.getEmail())) {
            throw new ConflictException(String.format(ErrorMessage.EMAIL_ALREADY_EXIST, registerRequest.getEmail()));
        }

        String encodedPassword = passwordEncoder.encode(registerRequest.getPassword());

        Role role = roleRepository.findByName(RoleType.ROLE_CUSTOMER).orElseThrow(() -> new ResourceNotFoundException(
                String.format(ErrorMessage.ROLE_NOT_FOUND_MESSAGE, RoleType.ROLE_CUSTOMER.name())));

        Set<Role> roles = new HashSet<>();
        roles.add(role);

        Customer customer = new Customer();
        customer.setFullName(registerRequest.getFullName());
        customer.setIdentityNumber(registerRequest.getIdentityNumber());
        customer.setEmail(registerRequest.getEmail());
        customer.setPassword(encodedPassword);
        customer.setSalary(registerRequest.getSalary());
        customer.setPhoneNumber(registerRequest.getPhoneNumber());
        customer.setRoles(roles);

        customerRepository.save(customer);
    }

    public List<CustomerDTO> getAllUsers() {
        List<Customer> customers = customerRepository.findAll();
        return customerMapper.map(customers);
    }

    public CustomerDTO findById(Long id) {
        Customer user = customerRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_MESSAGE, id)));

        return customerMapper.customerToCustomerDTO(user);
    }

    public void updatePassword(Long id, UpdatePasswordRequest passwordRequest) {
        Optional<Customer> userOpt = customerRepository.findById(id);
        Customer customer = userOpt.get();

        if (!passwordEncoder.matches(passwordRequest.getOldPassword(), customer.getPassword())) {
            throw new BadRequestException(ErrorMessage.PASSWORD_NOT_MATCHED);
        }

        String hashedPassword = passwordEncoder.encode(passwordRequest.getNewPassword());
        customer.setPassword(hashedPassword);

        customerRepository.save(customer);

    }

    @Transactional
    public void updateUser(Long id, CustomerUpdateRequest customerUpdateRequest) {
        boolean emailExist = customerRepository.existsByEmail(customerUpdateRequest.getEmail());

        Customer user = customerRepository.findById(id).get();

        if (emailExist && !customerUpdateRequest.getEmail().equals(user.getEmail())) {
            throw new ConflictException(String.format(ErrorMessage.EMAIL_ALREADY_EXIST,user.getEmail()));
        }

        customerRepository.update(id, customerUpdateRequest.getIdentityNumber(),
                customerUpdateRequest.getFullName(), customerUpdateRequest.getPhoneNumber(),
                customerUpdateRequest.getEmail(), customerUpdateRequest.getSalary());
    }



    public void updateCustomerAuth(Long id, AdminCustomerUpdateRequest adminCustomerUpdateRequest) {
        boolean emailExist = customerRepository.existsByEmail(adminCustomerUpdateRequest.getEmail());

        Customer user = customerRepository.findById(id).
                orElseThrow(()->new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_MESSAGE, id)));

        if (emailExist && !adminCustomerUpdateRequest.getEmail().equals(user.getEmail())) {
            throw new ConflictException(String.format(ErrorMessage.EMAIL_ALREADY_EXIST,user.getEmail()));
        }

        if (adminCustomerUpdateRequest.getPassword() == null) {
            adminCustomerUpdateRequest.setPassword(user.getPassword());
        } else {
            String encodedPassword = passwordEncoder.encode(adminCustomerUpdateRequest.getPassword());
            adminCustomerUpdateRequest.setPassword(encodedPassword);
        }

        Set<String> userStrRoles = adminCustomerUpdateRequest.getRoles();
        Set<Role> roles= convertRoles(userStrRoles);

        Customer updateCustomer = customerMapper.adminCustomerUpdateRequestToCustomer(adminCustomerUpdateRequest);

        updateCustomer.setId(user.getId());
        updateCustomer.setRoles(roles);

        customerRepository.save(updateCustomer);

    }

    public void removeById(Long id) {
        Customer user = customerRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_MESSAGE, id)));

        customerRepository.deleteById(id);
    }

    public Set<Role> convertRoles(Set<String> pRoles) {

        Set<Role> roles = new HashSet<>();

        if (pRoles == null) {
            Role userRole = roleRepository.findByName(RoleType.ROLE_CUSTOMER)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            String.format(ErrorMessage.ROLE_NOT_FOUND_MESSAGE, RoleType.ROLE_CUSTOMER.name())));

            roles.add(userRole);
        } else {
            pRoles.forEach(role -> {
                switch (role) {
                    case "Administrator":
                        Role adminRole = roleRepository.findByName(RoleType.ROLE_ADMIN)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                        String.format(ErrorMessage.ROLE_NOT_FOUND_MESSAGE, RoleType.ROLE_ADMIN.name())));

                        roles.add(adminRole);
                        break;

                    default:
                        Role userRole = roleRepository.findByName(RoleType.ROLE_CUSTOMER)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                        String.format(ErrorMessage.ROLE_NOT_FOUND_MESSAGE, RoleType.ROLE_CUSTOMER.name())));

                        roles.add(userRole);

                }
            });
        }

        return roles;
    }

    public CustomerDTO findByIdentity(Long identityNumber) {

        Customer user = customerRepository.findByIdentityNumber(identityNumber).orElseThrow(
                () -> new ResourceNotFoundException(String.format
                        (ErrorMessage.ID_NUMBER_NOT_FOUND_MESSAGE,identityNumber)));

        return customerMapper.customerToCustomerDTO(user);

    }

    public Map<Integer, String> application(Long identityNumber) {
        Customer user = customerRepository.findByIdentityNumber(identityNumber).orElseThrow(
                () -> new ResourceNotFoundException(String.format
                        (ErrorMessage.ID_NUMBER_NOT_FOUND_MESSAGE,identityNumber)));
        double montlyIncome = user.getSalary();
        int score = creditScoreCalculator(user.getIdentityNumber());

        Map<Integer, String> result = applicationResult(score,montlyIncome);

        return result;
    }

    private Map<Integer, String> applicationResult(Integer score, Double monthlyIncome) {

        int limit = 0;

        Integer creditLimit = 4;

        Map<Integer, String> result = new HashMap<>();

        if(score < 500){
            result.put(limit,"UNCONFIRMED");
            return  result;
        }else if(score >= 500 && score < 1000 && monthlyIncome.intValue() <= 5000){
            limit = 10000;
            result.put(limit,"CONFIRMED");
            return  result;
        }else if(score >= 500 && score < 1000 && monthlyIncome.intValue() > 5000){
            limit = 20000;
            result.put(limit,"CONFIRMED");
            return  result;
        }else if(score >= 1000){
            limit = (int) (monthlyIncome* creditLimit);
            result.put(limit,"CONFIRMED");
            return  result;
        }
        result.put(limit,"UNCONFIRMED");
        return  result;
    }

    private Integer creditScoreCalculator(Long identityNumber){

        int creditScore = 0;

        int lastDigit = (int) (identityNumber%10);

        switch (lastDigit){
            case 0:
                creditScore = 2000;
            case 2:
                creditScore = 550;
            case 4:
                creditScore = 1000;
            case 6:
                creditScore = 400;
            case 8:
                creditScore = 900;
            default:
                creditScore = 400;

        }

        return creditScore;
    }




}
