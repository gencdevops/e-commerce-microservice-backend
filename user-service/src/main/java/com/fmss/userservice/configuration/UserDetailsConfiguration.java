package com.fmss.userservice.configuration;

import com.fmss.userservice.repository.LdapRepository;
import com.fmss.userservice.model.entity.LdapUser;
import com.fmss.userservice.security.EcommerceUserDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
class UserDetailsConfiguration implements UserDetailsService {
    private final LdapRepository ldapRepository;


    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) {
        LdapUser ldapUser = ldapRepository.findUser(username);
        log.info("Retrieved ldap user {}", username);
        if (Objects.nonNull(ldapUser)) {
            return new EcommerceUserDetailService(ldapUser);
        }
        throw new UsernameNotFoundException(username);
    }
}
