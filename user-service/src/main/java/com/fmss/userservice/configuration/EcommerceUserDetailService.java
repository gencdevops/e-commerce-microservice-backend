package com.fmss.userservice.configuration;

import com.fmss.userservice.model.entity.Role;
import com.fmss.userservice.model.entity.User;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import static com.fmss.userservice.model.enums.UserStatus.ACTIVE;

@Getter
@ToString
public class EcommerceUserDetailService implements UserDetails {

    private final Set<GrantedAuthority> authorities;

    @ToString.Include
    private final User delegate;

    public EcommerceUserDetailService(User delegate) {
        this.delegate = delegate;
        this.authorities = delegate.getRoles().stream()
                .map(Role::getName)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return delegate.getPassword();
    }

    @Override
    public String getUsername() {
        return delegate.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return delegate.getUserStatus().equals(ACTIVE);
    }

    @Override
    public boolean isAccountNonLocked() {
        return delegate.getUserStatus().equals(ACTIVE);
    }

    public String getFullName() {
        return delegate.getUserName();
    }


    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return ACTIVE == delegate.getUserStatus();
    }

    public String getBeforePassword() {
        return delegate.getBeforePassword();
    }
}
