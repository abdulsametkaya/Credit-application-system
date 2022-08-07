package com.creditapplication.security.service;

import com.creditapplication.domain.Customer;
import com.creditapplication.exception.message.ErrorMessage;
import com.creditapplication.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private CustomerRepository customerRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		Customer user=customerRepository.findByEmail(email).orElseThrow(()->new
				UsernameNotFoundException(String.format(ErrorMessage.USER_NOT_FOUND_MESSAGE, email)));

		return UserDetailsImpl.build(user);
	}
}
