package com.missionx.questloggers.global.config.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class LoginUser implements UserDetails {

    private final Long userId;
    private final String email;
    private final String role;
    private final String apiKey;
    private int point;

    public LoginUser(Long userId, String email, String role, String apiKey, Integer point) {
        this.userId = userId;
        this.email = email;
        this.role = role;
        this.apiKey = apiKey;
        this.point = point;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(() -> "ROLE_USER");
    }

    public String getPassword() {
        return null;
    }

    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() { return true; }
    @Override
    public boolean isAccountNonLocked() { return true; }
    @Override
    public boolean isCredentialsNonExpired() { return true; }
    @Override
    public boolean isEnabled() { return true; }

    public Long getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public String getApiKey() {
        return apiKey;
    }

    public Integer getPoint() {
        return point;
    }

}
