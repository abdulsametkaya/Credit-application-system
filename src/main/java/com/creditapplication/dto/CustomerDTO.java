package com.creditapplication.dto;

import com.creditapplication.domain.Role;
import com.creditapplication.domain.enums.RoleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDTO {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private Set<String> roles;

    public void setRoles(Set<Role> roles) {
        Set<String> rolesStr = new HashSet<>();

        roles.forEach(r -> {
            if (r.getName().equals(RoleType.ROLE_ADMIN))
                rolesStr.add("Administrator");
            else
                rolesStr.add("Customer");
        });

        this.roles=rolesStr;
    }


}
