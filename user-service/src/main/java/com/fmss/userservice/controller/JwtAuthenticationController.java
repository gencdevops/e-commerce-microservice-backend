package com.fmss.userservice.controller;

import com.fmss.commondata.util.JwtUtil;
import com.fmss.userservice.security.EcommerceUserDetailService;
import com.fmss.userservice.configuration.UserDetailsConfig;
import com.fmss.userservice.model.dto.request.JwtRequest;
import com.fmss.userservice.model.dto.request.VerifyOtpRequest;
import com.fmss.userservice.model.dto.response.JwtResponse;
import com.fmss.userservice.repository.model.LdapUser;
import com.fmss.userservice.service.UserService;
import com.fmss.userservice.util.Validations;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequiredArgsConstructor
public class JwtAuthenticationController {

	private final AuthenticationManager authenticationManager;
	private final JwtUtil jwtUtil;
	private final UserDetailsConfig userDetailsConfig;
	private final UserService userService;

	@CrossOrigin(origins = "http://localhost:3000")
	@PostMapping("authenticate")
	public ResponseEntity<JwtResponse> createAuthenticationToken(@RequestBody JwtRequest jwtRequest, HttpServletRequest request) throws Exception {
		authenticate(jwtRequest.getUsername(), jwtRequest.getPassword());
		final var userDetails = userDetailsConfig.loadUserByUsername(jwtRequest.getUsername());
		final var userDetailService = (EcommerceUserDetailService) userDetails;
		LdapUser user = userDetailService.getDelegate();
		final String token = jwtUtil.generateToken(user.getUid(), user.getMail(), user.getGivenName());
		return ResponseEntity.ok(new JwtResponse(token));
	}

	@CrossOrigin(origins = "http://localhost:3000")
	@PostMapping("sent-otp")
	public ResponseEntity sentOtp(@RequestBody JwtRequest jwtRequest, HttpServletRequest request) throws Exception {
		userDetailsConfig.loadUserByUsername(jwtRequest.getUsername());
		userService.sentOtp(jwtRequest.getUsername());
		return ResponseEntity.ok().build();
	}

	@CrossOrigin(origins = "http://localhost:3000")
	@PostMapping("verify-otp")
	public ResponseEntity verifyOtp(@RequestBody VerifyOtpRequest verifyOtpRequest, HttpServletRequest request) throws Exception {
		if (userService.verifyOtp(verifyOtpRequest.getUsername(), verifyOtpRequest.getOtp())) {
			ResponseEntity.badRequest().build();
		}
		authenticate(verifyOtpRequest.getUsername(), verifyOtpRequest.getPassword());
		final var userDetails = userDetailsConfig.loadUserByUsername(verifyOtpRequest.getUsername());
		final var userDetailService = (EcommerceUserDetailService) userDetails;
		LdapUser user = userDetailService.getDelegate();
		final String token = jwtUtil.generateToken(user.getUid(), user.getMail(), user.getGivenName());
		return ResponseEntity.ok(new JwtResponse(token));
	}

	private void authenticate(String username, String password) throws Exception {
		Objects.requireNonNull(username);
		Objects.requireNonNull(password);

		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new DisabledException(Validations.ERR_USER_ACCOUNT_DISABLED);
		} catch (BadCredentialsException e) {
			throw new BadCredentialsException(Validations.ERR_WRONG_USERNAME_OR_PASSWORD);
		} catch (UsernameNotFoundException e) {
			throw new UsernameNotFoundException(Validations.ERR_USER_NOTFOUND);
		}
	}
}
