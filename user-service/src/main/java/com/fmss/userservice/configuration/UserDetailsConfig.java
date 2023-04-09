package com.fmss.userservice.configuration;

import com.fmss.userservice.model.entity.User;
import com.fmss.userservice.service.UserService;
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
    private final UserService userService;


    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) {
        User user = userService.findByEmail(username);
        if (Objects.nonNull(user)) {
            return new EcommerceUserDetailService(user);
        }
        throw new UsernameNotFoundException("error.userNotFound");
    }

    public EcommerceUserDetailService getUserById(Long userId) {
        final User user = userService.getById(userId);
        if (Objects.nonNull(user)) {
            return new EcommerceUserDetailService(user);
        }
        throw new UsernameNotFoundException("error.badCredentials");
    }
}
