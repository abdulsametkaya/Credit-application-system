package com.creditapplication.repository;

import com.creditapplication.domain.Role;
import com.creditapplication.domain.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> 	{
	 Optional<Role> findByName(RoleType name);
}
