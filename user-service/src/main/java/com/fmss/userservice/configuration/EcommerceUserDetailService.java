package com.fmss.userservice.configuration;

import com.fmss.userservice.repository.model.LdapUser;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

import static com.fmss.userservice.model.enums.UserStatus.ACTIVE;

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
        return delegate.getPassword();
    }

    @Override
    public String getUsername() {
        return delegate.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return delegate.getStatus().equals(ACTIVE);
    }

    @Override
    public boolean isAccountNonLocked() {
        return delegate.getStatus().equals(ACTIVE);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return delegate.getStatus().equals(ACTIVE);
    }

}
