package com.creditapplication.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 11, nullable=false)
    private Long identityNumber;

    @Column(length = 50, nullable=false)
    private String fullName;

    @Column(length=50,nullable = false,unique = true)
    private String email;

    @Column(length = 14, nullable=false)
    private String phoneNumber;

    @Column(length =12,nullable=false )
    private String password;

    @Column(length = 7, nullable=false)
    private Double salary;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="tbl_user_roles",
            joinColumns = @JoinColumn(name="user_id"),
            inverseJoinColumns = @JoinColumn(name="role_id"))
    private Set<Role> roles=new HashSet<>();


}
