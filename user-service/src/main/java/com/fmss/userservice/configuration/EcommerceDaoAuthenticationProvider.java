package com.fmss.userservice.configuration;

import com.advicemybackend.Validations;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.Assert;

/**
 * @author Muhammed ALAGOZ
 */

@RequiredArgsConstructor
public class EcommerceDaoAuthenticationProvider extends DaoAuthenticationProvider {
    private UserDetailsChecker postAuthenticationChecks;

    @Override
    public void setPostAuthenticationChecks(UserDetailsChecker postAuthenticationChecks) {
        this.postAuthenticationChecks = postAuthenticationChecks;
    }

    @Override
    public Authentication authenticate(Authentication authentication) {
        Assert.isInstanceOf(
                UsernamePasswordAuthenticationToken.class,
                authentication,
                messages.getMessage("AbstractUserDetailsAuthenticationProvider.onlySupports", "Only UsernamePasswordAuthenticationToken is supported")
        );

        String username = (authentication.getPrincipal() == null) ? "NONE_PROVIDED" : authentication.getName();
        boolean cacheWasUsed = true;
        UserDetails user = getUserCache().getUserFromCache(username);
        if (user == null) {
            cacheWasUsed = false;
            try {
                user = retrieveUser(username, (UsernamePasswordAuthenticationToken) authentication);
            } catch (UsernameNotFoundException notFound) {
                if (hideUserNotFoundExceptions) {
                    throw new BadCredentialsException(Validations.ERR_WRONG_USERNAME_OR_PASSWORD);
                } else {
                    throw notFound;
                }
            }
            Assert.notNull(user, "retrieveUser returned null - a violation of the interface contract");
        }
        try {
            additionalAuthenticationChecks(user, (UsernamePasswordAuthenticationToken) authentication);
        } catch (AuthenticationException exception) {
            if (cacheWasUsed) {
                cacheWasUsed = false;
                user = retrieveUser(username, (UsernamePasswordAuthenticationToken) authentication);
                additionalAuthenticationChecks(user, (UsernamePasswordAuthenticationToken) authentication);
            } else {
                throw exception;
            }
        }

        postAuthenticationChecks.check(user);
        if (!cacheWasUsed) {
            getUserCache().putUserInCache(user);
        }

        Object principalToReturn = user;
        if (isForcePrincipalAsString()) {
            principalToReturn = user.getUsername();
        }

        return createSuccessAuthentication(principalToReturn, authentication, user);
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) {
        if (authentication.getCredentials() == null) {
            throw new BadCredentialsException(Validations.ERR_WRONG_USERNAME_OR_PASSWORD);
        }
    }
}
