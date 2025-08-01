package com.assignment.userservice.entity;

import com.assignment.userservice.enums.UserRole;
import com.assignment.userservice.utils.Utils;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Users implements UserDetails {

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

    @Column(nullable = false, columnDefinition = "DATE")
    private LocalDate birthDate;

    @Column(nullable = false, length = 11)
    private String phoneNumber;

    @Column(nullable = false)
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role = UserRole.USER;

    @Column(nullable = false)
    private boolean deleted = false;

    @Builder
    public Users(String userId, String password, String name,
                 String citizenNumber, String phoneNumber, String address) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.citizenNumber = citizenNumber;
        this.birthDate = Utils.getBirthDateFromCitizenNumber(citizenNumber);
        this.phoneNumber = phoneNumber;
        this.address = address;
    }


    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }

    public void updateAddress(String newAddress) {
        this.address = newAddress;
    }

    public void delete() {
        this.deleted = true;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }
    @Override
    public String getUsername() {
        return userId;
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}
