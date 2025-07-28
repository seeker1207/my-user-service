package com.assignment.userservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true, nullable = false, updatable = false)
    private String userId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false, length = 13, updatable = false)
    private String citizenNumber;

    @Column(unique = true, nullable = false, length = 11)
    private String phoneNumber;

    @Column(nullable = false)
    private String address;

    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }

    public void updateAddress(String newAddress) {
        this.address = newAddress;
    }
}