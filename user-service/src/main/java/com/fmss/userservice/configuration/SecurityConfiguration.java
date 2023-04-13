package com.fmss.userservice.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fmss.commondata.util.JwtUtil;
import com.fmss.userservice.repository.LdapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.session.ChangeSessionIdAuthenticationStrategy;
import org.springframework.security.web.authentication.session.CompositeSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.ConcurrentSessionControlAuthenticationStrategy;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {

    private final UserDetailsConfig userDetailsConfig;
    private final JwtTokenFilter jwtTokenFilter;
    private final ObjectMapper objectMapper;
    private final LdapRepository ldapRepository;

    @Bean
    public EcommerceDaoAuthenticationProvider authenticationProvider() {
        EcommerceDaoAuthenticationProvider authProvider = new EcommerceDaoAuthenticationProvider(ldapRepository);

        authProvider.setUserDetailsService(userDetailsConfig);
        authProvider.setPasswordEncoder(passwordEncoder());
        authProvider.setPostAuthenticationChecks(new AccountStatusUserDetailsChecker());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors()
                .and()
                .csrf()
                .disable()

                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .sessionAuthenticationStrategy(sessionAuthenticationStrategy())

                .and()
                .authorizeHttpRequests()
                .requestMatchers("/authenticate").permitAll()
                .anyRequest().permitAll()
                .and()
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }

    @Bean
    public CompositeSessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new CompositeSessionAuthenticationStrategy(Arrays.asList(
                new ChangeSessionIdAuthenticationStrategy(),
                new ConcurrentSessionControlAuthenticationStrategy(sessionRegistry()),
                new RegisterSessionAuthenticationStrategy(sessionRegistry())
        ));
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean
    public RestAuthenticationEntryPoint authenticationEntryPoint() {
        return new RestAuthenticationEntryPoint(objectMapper);
    }

    @Bean
    public JwtUtil jwtUtil() {
        return new JwtUtil();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new LdapShaPasswordEncoder();
    }

}
