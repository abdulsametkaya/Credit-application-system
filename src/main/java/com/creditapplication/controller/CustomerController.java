package com.creditapplication.controller;

import com.creditapplication.dto.CustomerDTO;
import com.creditapplication.dto.request.AdminCustomerUpdateRequest;
import com.creditapplication.dto.request.CustomerUpdateRequest;
import com.creditapplication.dto.request.UpdatePasswordRequest;
import com.creditapplication.dto.response.CAResponse;
import com.creditapplication.dto.response.ApplicationResponse;
import com.creditapplication.dto.response.ResponseMessage;
import com.creditapplication.service.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class CustomerController {
	
	@Autowired
	private CustomerService customerService;
	
	@GetMapping("/auth/all")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<CustomerDTO>> getAllUsers(){
	    List<CustomerDTO> users= customerService.getAllUsers();
	    return ResponseEntity.ok(users);
	}
	

	@GetMapping
	@PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
	public ResponseEntity<CustomerDTO> getUserById(HttpServletRequest request){
		 Long id= (Long) request.getAttribute("id");
		CustomerDTO userDTO= customerService.findById(id);
		 
		 return ResponseEntity.ok(userDTO);
	}

	@GetMapping("/{identity}")
	@PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
	public ResponseEntity<CustomerDTO> getUserByIdentity(@PathVariable Long identity){
		CustomerDTO customerDTO = customerService.findByIdentity(identity);

		return ResponseEntity.ok(customerDTO);
	}

	
	//http://localhost:8080/user/{id}/auth
	//to get any user in the sytem, admin is able to use this method.
	@GetMapping("/{id}/auth")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<CustomerDTO> getUserByIdAdmin(@PathVariable Long id){
		CustomerDTO user= customerService.findById(id);
		return ResponseEntity.ok(user);
	}

	@GetMapping("/application/{identity}")
	@PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
	public ResponseEntity<ApplicationResponse> creditApplication(@PathVariable Long identity){

		Map<Integer, String> result = customerService.application(identity);

		ApplicationResponse applicationResponse = new ApplicationResponse();

		int num =0;
		for (Integer key : result.keySet()){
			num = key;
		}
		applicationResponse.setCreditLimit(num);
		applicationResponse.setResult(result.get(num));

		return ResponseEntity.ok(applicationResponse);
	}


	/*
	 * http://localhost:8080/user/auth { "newPassword":"testup",
	 * "oldPassword":"test1" }
	 */
	@PatchMapping("/auth")
	@PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
	public ResponseEntity<CAResponse> updatePassword(HttpServletRequest httpServletRequest,
													 @RequestBody UpdatePasswordRequest passwordRequest ){
		Long id=(Long) httpServletRequest.getAttribute("id");
		customerService.updatePassword(id, passwordRequest);

		CAResponse response=new CAResponse();
		response.setMessage(ResponseMessage.PASSWORD_CHANGED_MESSAGE);
		response.setSuccess(true);
		
		return ResponseEntity.ok(response);
	}
	
	
	@PutMapping
	@PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
	public ResponseEntity<CAResponse> updateUser(HttpServletRequest httpServletRequest,
												 @Valid @RequestBody CustomerUpdateRequest customerUpdateRequest){
		Long id=(Long) httpServletRequest.getAttribute("id");
		customerService.updateUser(id,customerUpdateRequest);

		CAResponse response=new CAResponse();
		response.setMessage(ResponseMessage.UPDATE_RESPONSE_MESSAGE);
		response.setSuccess(true);
		
		return ResponseEntity.ok(response);
		
	}

	
	//http://localhost:8080/user/2/auth
	@DeleteMapping("/{id}/auth")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<CAResponse> deleteUser(@PathVariable Long id){
		customerService.removeById(id);

		CAResponse response=new CAResponse();
		response.setMessage(ResponseMessage.DELETE_RESPONSE_MESSAGE);
		response.setSuccess(true);
		
		return ResponseEntity.ok(response);
	}
	
	//http://localhost:8080/user/5/auth
	@PutMapping("/{id}/auth")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<CAResponse> updateUserAuth(@PathVariable Long id,
													 @Valid @RequestBody AdminCustomerUpdateRequest adminCustomerUpdateRequest){

		customerService.updateCustomerAuth(id,adminCustomerUpdateRequest);

		CAResponse response=new CAResponse();
		response.setMessage(ResponseMessage.UPDATE_RESPONSE_MESSAGE);
		response.setSuccess(true);
		
		return ResponseEntity.ok(response);
		
	} 
	
	
	
	

}
