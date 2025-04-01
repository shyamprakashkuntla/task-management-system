package com.ve.task_management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ve.task_management.constants.ResponseWrapper;
import com.ve.task_management.model.Users;
import com.ve.task_management.payload.UsersRequest;
import com.ve.task_management.service.UsersServiceImpl;

@RestController
@RequestMapping("/api/auth")
public class RegistrationController {
	
	@Autowired
	private UsersServiceImpl userserviceImplementation;

	@PostMapping("/register")
	public ResponseWrapper<Users>  registerUser(@RequestBody UsersRequest user)
	{		
		 return userserviceImplementation.createUser(user);
	}
	
	
	@PostMapping("/login")
	public ResponseWrapper<String> loginUser(@RequestBody Users user)
	{
		return userserviceImplementation.verifyUser(user);
	}
	
	@GetMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
        }

        // Token is effectively "forgotten" by the client deleting it.
        return new ResponseEntity<>("Logout successful. Please remove the token from your frontend.", HttpStatus.OK);
    }
	
	/*
	@PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Loginrequest request) {
        String token = userserviceImplementation.verifyUser(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(new AuthResponse(token));
    }
    */
}
