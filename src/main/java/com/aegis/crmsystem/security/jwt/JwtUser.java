package com.aegis.crmsystem.security.jwt;

import com.aegis.crmsystem.models.User;
import com.aegis.crmsystem.repositories.UserRepository;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;

@Slf4j
@Data
public class JwtUser implements UserDetails{

    private final Long id;
    private final String firstName;
    private final String lastName;
    private final String password;
    private final String email;
    private final Date lastPasswordResetDate;
    private final Collection<? extends GrantedAuthority> authorities;
    private User user;

    public JwtUser(
            Long id,
            String firstName,
            String lastName,
            String email,
            String password,
            Collection<? extends GrantedAuthority> authorities,
            Date lastPasswordResetDate,
            User user
    ) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
        this.lastPasswordResetDate = lastPasswordResetDate;
        this.user = user;
    }

    @JsonIgnore
    public Long getId() {
        return id;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
