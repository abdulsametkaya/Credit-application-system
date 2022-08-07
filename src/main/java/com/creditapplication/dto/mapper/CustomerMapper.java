package com.creditapplication.dto.mapper;

import com.creditapplication.domain.Customer;
import com.creditapplication.dto.CustomerDTO;
import com.creditapplication.dto.request.AdminCustomerUpdateRequest;
import com.creditapplication.dto.request.CustomerUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
	
	CustomerDTO customerToCustomerDTO(Customer customer);
	List<CustomerDTO> map(List<Customer> customers);
	
	@Mapping(target="id", ignore=true)
	@Mapping(target="roles",ignore=true)
	Customer adminCustomerUpdateRequestToCustomer(AdminCustomerUpdateRequest request);

}
