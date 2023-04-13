package com.fmss.userservice.controller;

import com.fmss.commondata.dtos.response.OrderResponseDTO;
import com.fmss.commondata.util.JwtUtil;
import com.fmss.userservice.security.EcommerceUserDetailService;
import com.fmss.userservice.configuration.UserDetailsConfig;
import com.fmss.userservice.model.dto.request.JwtAuthenticationRequestDto;
import com.fmss.userservice.request.VerifyOtpRequest;
import com.fmss.userservice.model.dto.response.JwtResponseDto;
import com.fmss.userservice.model.LdapUser;
import com.fmss.userservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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



	@Operation(summary = "Create order")
	@ApiResponses(value =
	@ApiResponse(
			responseCode = "201",
			description = "Place order",
			content = @Content(
					schema = @Schema(implementation = OrderResponseDTO.class),
					mediaType = "application/json")))
	@PostMapping("/place-order")

	@CrossOrigin(origins = "http://localhost:3000")
	@PostMapping("authenticate")
	public ResponseEntity<JwtResponseDto> createAuthenticationToken(@RequestBody JwtAuthenticationRequestDto jwtAuthenticationRequestDto, HttpServletRequest request) throws Exception {
		authenticate(jwtAuthenticationRequestDto.getUsername(), jwtAuthenticationRequestDto.getPassword());
		final var userDetails = userDetailsConfig.loadUserByUsername(jwtAuthenticationRequestDto.getUsername());
		final var userDetailService = (EcommerceUserDetailService) userDetails;
		LdapUser user = userDetailService.getDelegate();
		final String token = jwtUtil.generateToken(user.getUid(), user.getMail(), user.getGivenName());
		return ResponseEntity.ok(new JwtResponseDto(token));
	}

	@CrossOrigin(origins = "http://localhost:3000")
	@PostMapping("sent-otp")
	public ResponseEntity sentOtp(@RequestBody JwtAuthenticationRequestDto jwtAuthenticationRequestDto, HttpServletRequest request) throws Exception {
		userDetailsConfig.loadUserByUsername(jwtAuthenticationRequestDto.getUsername());
		userService.sentOtp(jwtAuthenticationRequestDto.getUsername());
		return ResponseEntity.ok().build();
	}

	@CrossOrigin(origins = "http://localhost:3000")
	@PostMapping("verify-otp")
	public ResponseEntity verifyOtp(@RequestBody VerifyOtpRequest verifyOtpRequest, HttpServletRequest request) throws Exception {
		if (userService.verifyOtp(verifyOtpRequest.getUsername(), verifyOtpRequest. getOtp())) {
			ResponseEntity.badRequest().build();
		}
		authenticate(verifyOtpRequest.getUsername(), verifyOtpRequest.getPassword());
		final var userDetails = userDetailsConfig.loadUserByUsername(verifyOtpRequest.getUsername());
		final var userDetailService = (EcommerceUserDetailService) userDetails;
		LdapUser user = userDetailService.getDelegate();
		final String token = jwtUtil.generateToken(user.getUid(), user.getMail(), user.getGivenName());
		return ResponseEntity.ok(new JwtResponseDto(token));
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
