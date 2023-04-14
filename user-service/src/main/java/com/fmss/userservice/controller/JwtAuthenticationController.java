package com.fmss.userservice.controller;

import com.fmss.commondata.dtos.request.JwtAuthenticationRequestDto;
import com.fmss.commondata.dtos.response.JwtResponseDto;
import com.fmss.commondata.dtos.response.OrderResponseDTO;
import com.fmss.commondata.util.JwtUtil;
import com.fmss.userservice.configuration.UserDetailsConfiguration;
import com.fmss.userservice.model.LdapUser;
import com.fmss.userservice.request.VerifyOtpRequest;
import com.fmss.userservice.security.EcommerceUserDetailService;
import com.fmss.userservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

import static com.fmss.userservice.constants.UserConstants.*;

@RestController
@RequestMapping(API_PREFIX + API_VERSION_V1)
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationController {

	private final AuthenticationManager authenticationManager;
	private final JwtUtil jwtUtil;
	private final UserDetailsConfiguration userDetailsConfig;
	private final UserService userService;


	@Operation(summary = "authentication")
	@ApiResponses(value =
	@ApiResponse(
			responseCode = "201",
			description = "create auth",
			content = @Content(
					schema = @Schema(implementation = OrderResponseDTO.class),
					mediaType = "application/json")))
	@ResponseStatus(HttpStatus.CREATED)
	@CrossOrigin(origins = "http://localhost:3000")
	@PostMapping("/authenticate")
	public ResponseEntity<JwtResponseDto> createAuthenticationToken(@RequestBody JwtAuthenticationRequestDto jwtAuthenticationRequestDto, HttpServletRequest request) throws Exception {
		authenticate(jwtAuthenticationRequestDto.getUsername(), jwtAuthenticationRequestDto.getPassword());
		log.info("create authentication user :{}", jwtAuthenticationRequestDto.getUsername());
		final var userDetails = userDetailsConfig.loadUserByUsername(jwtAuthenticationRequestDto.getUsername());
		final var userDetailService = (EcommerceUserDetailService) userDetails;
		LdapUser user = userDetailService.getDelegate();
		final String token = jwtUtil.generateToken(user.getUid(), user.getMail(), user.getGivenName());
		return ResponseEntity.ok(new JwtResponseDto(token));
	}

	@Operation(summary = "Otp send")
	@ApiResponses(value =
	@ApiResponse(
			responseCode = "201",
			description = "send otp",
			content = @Content(
					schema = @Schema(implementation = OrderResponseDTO.class),
					mediaType = "application/json")))
	@ResponseStatus(HttpStatus.ACCEPTED)
	@CrossOrigin(origins = "http://localhost:3000")
	@PostMapping("sent-otp")
	public ResponseEntity sentOtp(@RequestBody JwtAuthenticationRequestDto jwtAuthenticationRequestDto, HttpServletRequest request) throws Exception {
		userDetailsConfig.loadUserByUsername(jwtAuthenticationRequestDto.getUsername());
		userService.sentOtp(jwtAuthenticationRequestDto.getUsername());
		log.info("send otp user :{}", jwtAuthenticationRequestDto.getUsername());
		return ResponseEntity.ok().build();
	}

	@CrossOrigin(origins = "http://localhost:3000")
	@PostMapping("verify-otp")
	public ResponseEntity verifyOtp(@RequestBody VerifyOtpRequest verifyOtpRequest, HttpServletRequest request) throws Exception {
		if (userService.verifyOtp(verifyOtpRequest.getUsername(), verifyOtpRequest. getOtp())) {
			ResponseEntity.badRequest().build();

		}
		authenticate(verifyOtpRequest.getUsername(), verifyOtpRequest.getPassword());
		log.info("authentication verified user :{}", verifyOtpRequest.getUsername());
		final var userDetails = userDetailsConfig.loadUserByUsername(verifyOtpRequest.getUsername());
		final var userDetailService = (EcommerceUserDetailService) userDetails;
		LdapUser user = userDetailService.getDelegate();
		final String token = jwtUtil.generateToken(user.getUid(), user.getMail(), user.getGivenName());
		log.info("done token generated  :{}", verifyOtpRequest.getUsername());
		return ResponseEntity.ok(new JwtResponseDto(token));
	}

	private void authenticate(String username, String password) throws Exception {
		Objects.requireNonNull(username);
		Objects.requireNonNull(password);

		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
			log.info("done authenticate user :{}", username);
		} catch (DisabledException e) {
			log.error("done authentication failed user :{}", username);
			throw new DisabledException(USER_ACCOUNT_DISABLED);

		} catch (BadCredentialsException e) {
			log.error("done authentication failed user :{}", username);
			throw new BadCredentialsException(WRONG_USERNAME_OR_PASSWORD);
		} catch (UsernameNotFoundException e) {
			log.error("done authentication failed user :{}", username);
			throw new UsernameNotFoundException(USER_NOT_FOUND);
		}
	}
}
