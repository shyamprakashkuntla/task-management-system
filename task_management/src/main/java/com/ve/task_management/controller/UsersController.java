package com.ve.task_management.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.ve.task_management.constants.ResponseWrapper;
import com.ve.task_management.model.Users;
import com.ve.task_management.payload.UsersRequest;
import com.ve.task_management.payload.UsersResponse;
import com.ve.task_management.service.UsersService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@Tag(name="USER CONTROLLER",description ="Get,update,Read all,Read by ID,Delete by Id")
public class UsersController {
	@Autowired
	UsersService  userService;
	@Autowired 
	private ModelMapper modelMapper;
	
	@GetMapping("/getAllUsers")
    public ResponseWrapper<List<UsersResponse>> getAllUsers() {
			return userService.getAllUsers();
	}

	
    @GetMapping("/User/{userId}")
    public ResponseWrapper<UsersResponse> getUserById(@PathVariable Integer userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Users loggedInUser = userService.getUserByEmail(authentication.getName()).getData();
 
        try {
        // Admin can access any user's data
        if (loggedInUser.getUserRole().equalsIgnoreCase("ADMIN")) {
            ResponseWrapper<Users> userResponse = userService.getUserById(userId);

            if (!userResponse.isSuccess()) {
                return new ResponseWrapper<>(userResponse.getStatusCode(),userResponse.getMessage(),false);
            }

            UsersResponse response = modelMapper.map(userResponse.getData(), UsersResponse.class);
            return new ResponseWrapper<>(userResponse.getStatusCode(),userResponse.getMessage(), true, response);
        }

        // Regular users can only access their own data
        if (loggedInUser.getUserId().equals(userId)) {  // 👈 Check if the logged-in user is requesting their own data
            ResponseWrapper<Users> userResponse = userService.getUserById(userId);

            if (!userResponse.isSuccess()) {
            	return new ResponseWrapper<>(userResponse.getStatusCode(),userResponse.getMessage(),false);
            }

            UsersResponse response = modelMapper.map(userResponse.getData(), UsersResponse.class);
            return new ResponseWrapper<>(userResponse.getStatusCode(),userResponse.getMessage(), true, response);
        }

        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to access this data");
        }
        catch (NoSuchElementException e) {
            return new ResponseWrapper<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), false);

        } catch (ResponseStatusException e) {
            return new ResponseWrapper<>(e.getStatusCode().value(), e.getReason(), false);

        } catch (Exception e) {
            return new ResponseWrapper<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Unexpected error: " + e.getMessage(), false);
        }
    }
    
    @DeleteMapping("/deleteUser/{userId}")
    public ResponseWrapper<Void> deleteUser(@PathVariable Integer userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
//        ResponseWrapper<Users> loggedInUser = userService.getUserByEmail(authentication.getName());
        Users loggedInUser = userService.getUserByEmail(authentication.getName()).getData();
        if (loggedInUser.getUserRole().equalsIgnoreCase("ADMIN") || loggedInUser.getUserId().equals(userId)) {
            log.info("User {} deleted user with ID: {}", loggedInUser.getEmail(), userId);
            return userService.deleteUser(userId);
            
        } 

        log.warn("Unauthorized delete attempt by user: {}", loggedInUser.getEmail());
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to delete this user.");
    }
    
    
    
    @PutMapping("/updateUser/{userId}")
    public ResponseWrapper<Users> updateUser(
            @PathVariable Integer userId,
            @Valid @RequestBody UsersRequest userRequest) 
    {
        // Get the authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Users loggedInUser = userService.getUserByEmail(authentication.getName()).getData();

        // Check if the user is allowed to update (Admin or matching User ID)
        if (canUpdateUser(loggedInUser, userId)) {
            log.info("User {} updated user with ID: {}", loggedInUser.getEmail(), userId);
            return userService.updateUser(userId, userRequest);
        } 

        // Unauthorized access
        log.warn("Unauthorized update attempt by user: {}", loggedInUser.getEmail());
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to update this user.");
    }

    // Helper method for cleaner code
    private boolean canUpdateUser(Users loggedInUser, Integer userId) {
        return loggedInUser.getUserId().equals(userId) || 
               loggedInUser.getUserRole().equals("ADMIN");
    }
	
}