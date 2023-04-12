package com.fmss.userservice.service;

import com.fmss.userservice.configuration.EcommerceUserDetailService;
import com.fmss.userservice.configuration.JwtTokenUtil;
import com.fmss.userservice.configuration.mail.MailingService;
import com.fmss.userservice.exeption.RestException;
import com.fmss.userservice.model.dto.request.UserRegisterRequestDto;
import com.fmss.userservice.model.entity.User;
import com.fmss.userservice.repository.UserRepository;
import com.fmss.userservice.util.Validations;
import com.google.common.primitives.Longs;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static com.fmss.userservice.util.AppSettingsKey.CREATE_PASSWORD_URL_FORMAT;
import static com.fmss.userservice.util.AppSettingsKey.RESET_PASSWORD_URL_FORMAT;
import static com.fmss.userservice.util.Validations.ERR_INVALID_FORGOT_PASSWORD_TOKEN;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final MailingService mailingService;

    private final JwtTokenUtil jwtTokenUtil;

    @Transactional
    public void registerUser(UserRegisterRequestDto userRegisterRequestDto) {
        final var user = userRegisterRequestDto.toUser();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Transactional
    public boolean existByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Transactional(readOnly = true)
    public void sendForgotPasswordMail(String username) {
        final var user = userRepository.findByEmail(username);
        final String link = createForgotPasswordLink(user);
        mailingService.sendForgotPasswordEmail(user.getEmail(), user.getUserName(), link);
    }

    @Transactional
    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException(Validations.CUSTOMER_ID_INVALID));
    }

    @Transactional
    public User findByEmail(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username);
    }


    @Transactional(readOnly = true)
    public EcommerceUserDetailService getUserByForgotPasswordToken(String uid, String token) {
        final User user = getUserByEncodedUserId(uid);
        if (!StringUtils.equals(token, user.generateResetPasswordToken())) {
            throw new RestException(ERR_INVALID_FORGOT_PASSWORD_TOKEN);
        }
        return new EcommerceUserDetailService(user);
    }


    @Transactional
    public void resetPassword(String uid, String password) {
        changePassword(uid, password);
    }


    @Transactional
    public void changePassword(Long userId, String currentPassword, String newPassword) {
        final Optional<User> userOptional = userRepository.findById(userId);
        final User user = userOptional.orElseThrow(() -> new RestException("error.userNotFoundById"));
        if (passwordEncoder.matches(currentPassword, user.getPassword())) {
            changeUserPassword(user, newPassword);
        } else {
            throw new RestException("error.currentPasswordNotMatch");
        }
    }

    @Transactional
    public EcommerceUserDetailService getUserByCreatePasswordToken(String uid, String token) {
        final User user = getUserByEncodedUserId(uid);
        if (!StringUtils.equals(token, user.generateCreatePasswordToken())) {
            throw new RestException("error.createPasswordTokenInvalid");
        }
        return new EcommerceUserDetailService(user);
    }

    @Transactional
    public void createPassword(String uid, String password) {
        changePassword(uid, password);
    }

    private User getUserByEncodedUserId(String encodedUserId) {
        try {
            final byte[] bytes = Base64.decodeBase64(URLDecoder.decode(encodedUserId, StandardCharsets.UTF_8.name()));
            final long userIdLong = Longs.fromByteArray(bytes);
            final Optional<User> userOptional = userRepository.findById(userIdLong);
            return userOptional.orElseThrow(() -> new RestException(ERR_INVALID_FORGOT_PASSWORD_TOKEN));
        } catch (UnsupportedEncodingException e) {
            throw new RestException(ERR_INVALID_FORGOT_PASSWORD_TOKEN, e);
        }
    }

    private void changePassword(String encodedUserId, String password) {
        final User user = getUserByEncodedUserId(encodedUserId);
        changeUserPassword(user, password);
    }

    private void changeUserPassword(User user, String password) {
        final String currentPassword = user.getPassword();
        user.setBeforePassword(currentPassword);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.saveAndFlush(user);
    }

    private String createNewUserPasswordLink(User user) {
        final String token = user.generateCreatePasswordToken();
        String url = null;
        if (StringUtils.isEmpty(url)) {
            url = "http://localhost:8090";
        }
        return String.format(CREATE_PASSWORD_URL_FORMAT, url, token, createBase64UserId(user));
    }

    private String createBase64UserId(User user) {
        return Base64.encodeBase64URLSafeString(user.getId().getBytes());
    }

    private String createForgotPasswordLink(User user) {
        final String token = user.generateResetPasswordToken();
        String url = null;
        if (StringUtils.isEmpty(url)) {
            url = "http://localhost:8090";
        }
        return String.format(RESET_PASSWORD_URL_FORMAT, url, token, createBase64UserId(user));
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        return jwtTokenUtil.validateToken(token, userDetails.getUsername());
    }
}
