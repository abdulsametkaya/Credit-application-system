package com.creditapplication.repository;

import com.creditapplication.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

	Optional<Customer> findByEmail(String email);
	Boolean existsByEmail(String email);
	Optional<Customer> findByIdentityNumber(Long identityNumber);

	@Modifying
	@Query("UPDATE Customer u SET u.identityNumber=:identityNumber,u.fullName=:fullName, " +
			"u.phoneNumber=:phoneNumber, u.email=:email, u.salary=:salary WHERE u.id=:id")
	void update(@Param("id") Long id, @Param("identityNumber") Long IdentityNumber,
				@Param("fullName") String fullName , @Param("phoneNumber") String phoneNumber,
				@Param("email") String email, @Param("salary") Double salary);
	
}
