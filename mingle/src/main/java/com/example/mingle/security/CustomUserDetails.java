package com.example.mingle.security;

import com.example.mingle.domain.Guest;
import com.example.mingle.domain.Host;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {
    private String username;
    private String password;
    private String role;

    // 기존 생성자: Guest와 Host를 받는 생성자
    public CustomUserDetails(Guest guest) {
        this.username = guest.getIdid();
        this.password = guest.getPassword();
        this.role = "ROLE_USER";
    }

    public CustomUserDetails(Host host) {
        this.username = host.getIdid();
        this.password = host.getPassword();
        this.role = "ROLE_HOST";
    }

    public CustomUserDetails(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
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

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getRole() {
        return role;
    }
}
