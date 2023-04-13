package com.fmss.userservice.security;

import com.fmss.userservice.model.entity.LdapUser;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

@Getter
@ToString
public class EcommerceUserDetailService implements UserDetails {

    private final Set<GrantedAuthority> authorities;

    @ToString.Include
    private final LdapUser delegate;

    public EcommerceUserDetailService(LdapUser delegate) {
        this.delegate = delegate;
        this.authorities = Set.of(new SimpleGrantedAuthority("LDAP_USER"));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return delegate.getUserPassword();
    }

    @Override
    public String getUsername() {
        return delegate.getMail();
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

}
