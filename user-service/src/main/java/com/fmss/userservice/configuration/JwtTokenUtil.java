package com.fmss.userservice.configuration;

import com.advicemybackend.configuration.fault.ServiceException;
import com.advicemybackend.mapper.UserContactInfoMapper;
import com.advicemybackend.model.entity.advisor.Advisor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static java.util.Optional.ofNullable;

@Component
@RequiredArgsConstructor
public class JwtTokenUtil implements Serializable {

    private static final long serialVersionUID = -2550185165626007488L;

    public static final long JWT_TOKEN_VALIDITY = 24 * 60 * 60;

    private final UserContactInfoMapper userContactInfoMapper;
    private final ObjectMapper objectMapper;

    @Value("${jwt.secret}")
    private String secret;

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getIssuedAtDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getIssuedAt);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    private Boolean ignoreTokenExpiration(String token) {
        return false;
    }

    public String generateToken(EcommerceUserDetailService userDetails) {
        Map<String, Object> claims = new HashMap<>();
        final var user = userDetails.getDelegate();
        final var userId = user.getId();
        final var advisor = user.getAdvisor();
        final var contactInfoDtoOptional = ofNullable(user.getUserContactInfo()).map(userContactInfoMapper::toDto);
        final var advisorIdOptional = ofNullable(advisor).map(Advisor::getId);

        if (user.isBlacklisted()) {
            throw new ServiceException("Hata alındı.");
        }
        claims.put("customerId", userId);
        contactInfoDtoOptional.ifPresent(userContactInfoDto -> {
            try {
                claims.put("userInfo", objectMapper.writeValueAsString(userContactInfoDto));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
        advisorIdOptional.ifPresent(advisorId -> claims.put("advisorId", advisorId));

        return doGenerateToken(claims, userDetails.getUsername());
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public Boolean canTokenBeRefreshed(String token) {
        return (!isTokenExpired(token) || ignoreTokenExpiration(token));
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
