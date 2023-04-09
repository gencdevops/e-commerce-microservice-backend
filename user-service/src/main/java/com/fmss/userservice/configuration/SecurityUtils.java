package com.fmss.userservice.configuration;

import com.fmss.userservice.model.entity.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.validation.constraints.NotNull;
import java.util.Optional;

import static java.util.Optional.ofNullable;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SecurityUtils {

    public static String getUserEmail() {
        return getUser()
                .map(User::getEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found."));
    }

    @NotNull
    public static Optional<User> getUser() {
        return getUserDetails()
                .map(EcommerceUserDetailService::getDelegate);
    }

    @NotNull
    public static Optional<Authentication> getAuthentication() {
        return ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication);
    }

    @NotNull
    public static Optional<EcommerceUserDetailService> getUserDetails() {
        return getAuthentication()
                .map(Authentication::getPrincipal)
                .filter(EcommerceUserDetailService.class::isInstance)
                .map(EcommerceUserDetailService.class::cast);
    }

    public static void clearAuthentication() {
        SecurityContextHolder.getContext().setAuthentication(null);
    }

    public static void setAuthenticated(EcommerceUserDetailService ecommerceUserDetailService) {
        final UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(ecommerceUserDetailService, ecommerceUserDetailService.getPassword(), ecommerceUserDetailService.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
