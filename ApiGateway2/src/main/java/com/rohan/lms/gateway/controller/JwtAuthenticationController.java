package com.rohan.lms.gateway.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.rohan.lms.gateway.model.AuthenticationStatus;
import com.rohan.lms.gateway.model.ErrorResponseDto;
import com.rohan.lms.gateway.model.JwtRequest;
import com.rohan.lms.gateway.model.JwtResponse;
import com.rohan.lms.gateway.model.User;
import com.rohan.lms.gateway.repository.UserRepository;
import com.rohan.lms.gateway.security.JwtTokenUtil;

@RestController
public class JwtAuthenticationController {

	@Autowired
	private final JwtTokenUtil jwtTokenUtil;

	@Autowired
	private final UserRepository userRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public JwtAuthenticationController(JwtTokenUtil jwtTokenUtil, UserRepository userRepo) {
		this.jwtTokenUtil = jwtTokenUtil;
		this.userRepo = userRepo;
	}

	@PostMapping("/auth")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) {

		AuthenticationStatus status = authenticate(authenticationRequest.getUsername(),
				authenticationRequest.getPassword());
		
		if (!status.getIsAuthenticated()) {
			List<String> details = new ArrayList<>();
			details.add(status.getMessage());
			ErrorResponseDto error = new ErrorResponseDto(new Date(), "UNAUTHORIZED", details, "uri");
			return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
		}
		
		final String token = jwtTokenUtil.generateToken(authenticationRequest.getUsername());

		return ResponseEntity.ok(new JwtResponse(token));
	}

	private AuthenticationStatus authenticate(String username, String password) {

		AuthenticationStatus status;

		Optional<User> user = userRepo.findByEmail(username);

		if (user.isPresent()) {
			if (passwordEncoder.matches(password, user.get().getPwd())) {
				status =  new AuthenticationStatus(true, "Authentication Successful");
			} else {
				status =  new AuthenticationStatus(false, "Invalid Password");
			}
		} else {
			status =  new AuthenticationStatus(false, "No user registered with this details");
		}
		
		return status;

	}

}
