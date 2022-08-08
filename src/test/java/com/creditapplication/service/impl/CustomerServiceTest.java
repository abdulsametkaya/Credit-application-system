package com.creditapplication.service.impl;

import com.creditapplication.domain.Customer;
import com.creditapplication.domain.Role;
import com.creditapplication.domain.enums.RoleType;
import com.creditapplication.dto.CustomerDTO;
import com.creditapplication.dto.request.RegisterRequest;
import com.creditapplication.repository.CustomerRepository;
import com.creditapplication.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    @BeforeClass
    public void setup(){


    }

    @Test
    void getAllCustomers() {

        Role role1 = new Role(1, RoleType.ROLE_CUSTOMER);
        Set<Role> roleSet1 = new HashSet<>();
        roleSet1.add(role1);

        Role role2 = new Role(2, RoleType.ROLE_ADMIN);
        Set<Role> roleSet2 = new HashSet<>();
        roleSet2.add(role2);
        roleSet2.add(role1);


        Customer customer1 = new Customer(1L, 23902933421L,"Customer1","cstm@gmail.com"
                ,"123asc","(203) 525-1221",10000D,roleSet1);
        Customer customer2 = new Customer(2L, 23902933421L,"Admin1","sgtm@gmail.com"
                ,"123bdsc","(203) 525-1451",70000D,roleSet2);

        List<Customer> customers = new ArrayList<>();
        customers.add(customer1);
        customers.add(customer2);

        // stub - when
        when(customerRepository.findAll()).thenReturn(customers);

        // then
        List<CustomerDTO> allCustomers = customerService.getAllUsers();

        Assert.assertEquals(customers.size(), allCustomers.size());
    }

    @Test
    void getCustomer() {

        Role role1 = new Role(1, RoleType.ROLE_CUSTOMER);
        Set<Role> roleSet1 = new HashSet<>();
        roleSet1.add(role1);

        // init step
        Customer customer1 = new Customer(1L, 23902933421L,"Customer1","cstm@gmail.com"
                ,"123asc","(203) 525-1221",10000D,roleSet1);

        // stub - when step
        Optional<Customer> expectedOptCustomer = Optional.of(customer1);
        when(customerRepository.findById(customer1.getId())).thenReturn(expectedOptCustomer);

        // then step
        CustomerDTO actualCustomer = customerService.findById(customer1.getId());

        // valid step
        assertEquals(customer1, actualCustomer);
    }



    @Test
    void register() {

        Role role1 = new Role(1, RoleType.ROLE_CUSTOMER);
        Set<Role> roleSet1 = new HashSet<>();
        roleSet1.add(role1);

        // init

        RegisterRequest registerRequest = new RegisterRequest( 23902933421L,"Customer1",
                "cstm@gmail.com","123asc","(203) 525-1221",10000D);

        Customer customer1 = new Customer(1L, 23902933421L,"Customer1","cstm@gmail.com"
                ,"123asc","(203) 525-1221",10000D,roleSet1);

        // stub - when
        when(customerRepository.save(customer1)).thenReturn(customer1);

        // then
        customerService.register(registerRequest);

        verify(customerRepository, times(1)).save(customer1);
    }

    @Test
    void updateCustomer() {
    }

    @Test
    void deleteCustomer() {
    }

    @Test
    void findCustomerByNationalId() {
    }
}