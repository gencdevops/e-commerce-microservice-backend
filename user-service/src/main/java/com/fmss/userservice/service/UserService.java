package com.fmss.userservice.service;

import com.fmss.userservice.configuration.mail.MailingService;
import com.fmss.userservice.exeption.RestException;
import com.fmss.userservice.jwt.JwtTokenUtil;
import com.fmss.userservice.model.dto.request.UserRegisterRequestDto;
import com.fmss.userservice.model.entity.User;
import com.fmss.userservice.repository.LdapRepository;
import com.fmss.userservice.repository.UserRepository;
import com.fmss.userservice.repository.model.LdapUser;
import com.fmss.userservice.security.EcommerceUserDetailService;
import com.fmss.userservice.util.Validations;
import com.google.common.primitives.Longs;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.InvalidNameException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Optional;
import java.util.Random;

import static com.fmss.userservice.util.AppSettingsKey.CREATE_PASSWORD_URL_FORMAT;
import static com.fmss.userservice.util.AppSettingsKey.RESET_PASSWORD_URL_FORMAT;
import static com.fmss.userservice.util.Validations.ERR_INVALID_FORGOT_PASSWORD_TOKEN;


@Service
@RequiredArgsConstructor
public class UserService {
    public static final String OTP_REDIS_KEY = "-otp";
    private final UserRepository userRepository;
    @Lazy
    private final PasswordEncoder passwordEncoder;
    private final MailingService mailingService;
    private final JwtTokenUtil jwtTokenUtil;

    private final LdapRepository ldapRepository;
    
    private final RedisTemplate<String, Object> redisTemplate;

    //@PostConstruct
    public void init() throws InvalidNameException {
//        final var ldapUser = new LdapUser();
//        //ldapUser.setId(new LdapName("uid=sercan1,o=64346c9136393df65a68908f,dc=jumpcloud,dc=com"));
//        ldapUser.setGivenName("mahoni");
//        ldapUser.setUid("mahoni");
//        ldapUser.setSn("mahoni");
//        ldapUser.setMail("muhammed.alagoz@fmsstech.com");
//        ldapUser.setUserPassword(passwordEncoder.encode("1234"));
//
//        ldapRepository.create(ldapUser);

        sendForgotPasswordMail("muhammed.alagoz@fmsstech.com");
    }

    @Transactional
    public void registerUser(UserRegisterRequestDto userRegisterRequestDto) {
        final var user = userRegisterRequestDto.toUser();
        user.setUserPassword(passwordEncoder.encode(userRegisterRequestDto.password()));
        ldapRepository.create(user);
    }

    @Transactional
    public boolean existByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public void sendForgotPasswordMail(String username) {
        final var ldapUser = ldapRepository.findUser(username);
        final String link = createForgotPasswordLink(ldapUser);
        mailingService.sendForgotPasswordEmail(ldapUser.getMail(), ldapUser.getGivenName(), ldapUser.getSn(), link);
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
        return null;//TODO new EcommerceUserDetailService(user);
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
        return null;//TODO new EcommerceUserDetailService(user);
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

    private String createNewUserPasswordLink(LdapUser ldapUser) {
        final String token = ldapUser.generateCreatePasswordToken();
        String url = null;
        if (StringUtils.isEmpty(url)) {
            url = "http://localhost:3000";
        }
        return String.format(CREATE_PASSWORD_URL_FORMAT, url, token, createBase64UserUid(ldapUser));
    }

    private String createBase64UserUid(LdapUser ldapUser) {
        return Base64.encodeBase64URLSafeString(ldapUser.getUid().getBytes());
    }

    private String createForgotPasswordLink(LdapUser user) {
        final String token = user.generateResetPasswordToken();
        String url = null;
        if (StringUtils.isEmpty(url)) {
            url = "http://localhost:3000";
        }
        return String.format(RESET_PASSWORD_URL_FORMAT, url, token, createBase64UserUid(user));
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        return jwtTokenUtil.validateToken(token, userDetails.getUsername());
    }

    private String generateOtpToken(LdapUser user) {
        final var random = new Random();
        final var randomNumber = random.nextInt(900000) + 100000;
        redisTemplate.opsForValue().set(user.getUid() + OTP_REDIS_KEY, String.valueOf(randomNumber), Duration.ofMinutes(2));
        return String.valueOf(randomNumber);
    }

    public void sentOtp(String email) {
        final var ldapUser = ldapRepository.findUser(email);
        final String otp = generateOtpToken(ldapUser);
        mailingService.sendOtpEmail(ldapUser.getMail(), ldapUser.getGivenName(), ldapUser.getSn(), otp);
    }

    public boolean verifyOtp(String email, String otp) {
        try {
            final var user = ldapRepository.findUser(email);
            String sessionOtp = (String) redisTemplate.opsForValue().get(user.getUid() + OTP_REDIS_KEY);
            if (!StringUtils.equals(otp, sessionOtp)) {
                return false;
            }
        } catch (Exception ex) {
            return false;
        }
        return true;
    }
}
