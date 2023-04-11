package com.fmss.userservice.configuration;

import com.fmss.userservice.repository.LdapRepository;
import com.fmss.userservice.repository.model.LdapUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class UserDetailsConfig implements UserDetailsService {
    private final LdapRepository ldapRepository;


    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) {
        LdapUser ldapUser = ldapRepository.findUser(username);
        if (Objects.nonNull(ldapUser)) {
            return new EcommerceUserDetailService(ldapUser);
        }
        throw new UsernameNotFoundException("error.userNotFound");
    }
}
