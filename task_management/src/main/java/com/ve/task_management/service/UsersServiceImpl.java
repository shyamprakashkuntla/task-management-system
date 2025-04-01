package com.ve.task_management.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ve.task_management.config.JwtUtil;
import com.ve.task_management.config.SecurityConfigProperties;
import com.ve.task_management.constants.CommonConstants;
import com.ve.task_management.constants.ResponseWrapper;
import com.ve.task_management.model.Users;
import com.ve.task_management.payload.UsersRequest;
import com.ve.task_management.payload.UsersResponse;
import com.ve.task_management.repository.UsersRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UsersServiceImpl implements UsersService {

    @Autowired
    private ModelMapper modelMapper;
    
    @Value("${security.max-attempts}")
    public long MAX_ATTEMPTS;
    
    @Value("${security.lockout-minutes}")
    public long LOCKOUT_HOURS;

    @Autowired
    private UsersRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final SecurityConfigProperties configProperties;
    private final EmailService emailService;

    public UsersServiceImpl(BCryptPasswordEncoder bCryptPasswordEncoder,
                            AuthenticationManager authenticationManager,
                            JwtUtil jwtUtil,
                            SecurityConfigProperties configProperties,
                            EmailService emailService) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.configProperties = configProperties;
        this.emailService = emailService;
    }

    /**
     * Create a new user.
     */
    public ResponseWrapper<Users> createUser(UsersRequest user) {
        try {
            // Encrypt password
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

            Users users = modelMapper.map(user, Users.class);
            user.setEmail(user.getEmail().toLowerCase());

            // Save the user
            Users savedUser = userRepository.save(users);

            // Send confirmation email
            emailService.sendRegistrationEmail(savedUser.getEmail(), savedUser.getUserName());

            return new ResponseWrapper<>(HttpStatus.CREATED, CommonConstants.USER_CREATED_SUCCESSFULLY, true, savedUser);
        } catch (Exception e) {
            log.error("Error while creating user: {}", e.getMessage());
            return new ResponseWrapper<>(HttpStatus.INTERNAL_SERVER_ERROR, CommonConstants.UNEXPECTED_ERROR, false);
        }
    }

    
    /**
     * Verify user with authentication.
     */
    /*
    public ResponseWrapper<String> verifyUser(Users user) {
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(
                            user.getEmail(), user.getPassword()));

            if (authentication.isAuthenticated()) {
                String token = jwtUtil.generateToken(user.getEmail());
                return new ResponseWrapper<>(HttpStatus.ACCEPTED, CommonConstants.AUTHENTICATION_SUCCESSFUL, true, token);
            } else {
                return new ResponseWrapper<>(HttpStatus.UNAUTHORIZED, CommonConstants.AUTHENTICATION_FAILED, false);
            }
        } catch (AuthenticationException e) {
            log.error("Invalid credentials: {}", e.getMessage());
            return new ResponseWrapper<>(HttpStatus.UNAUTHORIZED, CommonConstants.INVALID, false);
        } catch (Exception e) {
            log.error("Unexpected error during verification: {}", e.getMessage());
            return new ResponseWrapper<>(HttpStatus.INTERNAL_SERVER_ERROR, CommonConstants.UNEXPECTED_ERROR, false);
        }
    }
    	*/
    
    //trying the login lockout method 
    

    /**
     * Login method with lockout mechanism
     */
    public ResponseWrapper<String> verifyUser(Users user) {
        try {
            Optional<Users> optionalUser = userRepository.findByEmail(user.getEmail());

            if (optionalUser.isEmpty()) {
                return new ResponseWrapper<>(HttpStatus.NOT_FOUND, "User not found", false);
            }

            Users dbUser = optionalUser.get();

            // Check if the account is locked
            if (dbUser.isAccountLocked()) {
                if (dbUser.getLockoutTime().isBefore(LocalDateTime.now())) {
                    // Unlock the account after 12 hours
                    dbUser.setAccountLocked(false);
                    dbUser.setFailedLoginAttempts(0);
                    dbUser.setLockoutTime(null);
                    userRepository.save(dbUser);
                    log.info("User {} unlocked automatically after 12 hours.", dbUser.getEmail());
                } else {
                    return new ResponseWrapper<>(HttpStatus.LOCKED, "Account is locked. Try again later.", false);
                }
            }

            // Authenticate the user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getEmail(), user.getPassword())
            );

            if (authentication.isAuthenticated()) {
                // Reset failed attempts on successful login
                dbUser.setFailedLoginAttempts(0);
                dbUser.setAccountLocked(false);
                dbUser.setLockoutTime(null);
                userRepository.save(dbUser);

                // Generate JWT token
                String token = jwtUtil.generateToken(user.getEmail());
                return new ResponseWrapper<>(HttpStatus.ACCEPTED, "Login successful", true, token);
            }

        } catch (AuthenticationException e) {
            // Handle invalid credentials
            log.warn("Failed login attempt for user: {}", user.getEmail());

            Optional<Users> optionalUser = userRepository.findByEmail(user.getEmail());
            if (optionalUser.isPresent()) {
                Users dbUser = optionalUser.get();

                // Increment failed login attempts
                dbUser.setFailedLoginAttempts(dbUser.getFailedLoginAttempts() + 1);

                // Lock the account after 3 failed attempts
                if (dbUser.getFailedLoginAttempts() >= MAX_ATTEMPTS) {
                    dbUser.setAccountLocked(true);
                    dbUser.setLockoutTime(LocalDateTime.now().plusMinutes(LOCKOUT_HOURS));
                    log.warn("User {} is locked out due to multiple failed attempts.", dbUser.getEmail());
                }

                userRepository.save(dbUser);
            }

            return new ResponseWrapper<>(HttpStatus.UNAUTHORIZED, "Invalid credentials", false);

        } catch (Exception e) {
            log.error("Unexpected error during verification: {}", e.getMessage());
            return new ResponseWrapper<>(HttpStatus.INTERNAL_SERVER_ERROR, CommonConstants.UNEXPECTED_ERROR, false);
        }

        return new ResponseWrapper<>(HttpStatus.UNAUTHORIZED, "Invalid credentials", false);
    }

    
    /**
     * Retrieve all users.
     */
    @Override
    public ResponseWrapper<List<UsersResponse>> getAllUsers() {
        try {
            List<Users> users = userRepository.findAll();

            if (users.isEmpty()) {
                return new ResponseWrapper<>(HttpStatus.NO_CONTENT, "No users found", true);
            }

            List<UsersResponse> response = users.stream()
                    .map(user -> modelMapper.map(user, UsersResponse.class))
                    .collect(Collectors.toList());

            return new ResponseWrapper<>(HttpStatus.OK, "Fetched all users successfully", true, response);
        } catch (Exception e) {
            log.error("Error while fetching users: {}", e.getMessage());
            return new ResponseWrapper<>(HttpStatus.INTERNAL_SERVER_ERROR, CommonConstants.UNEXPECTED_ERROR, false);
        }
    }

    /**
     * Retrieve a user by ID.
     */
    @Override
    public ResponseWrapper<Users> getUserById(Integer userId) {
        try {
            Optional<Users> userOpt = userRepository.findById(userId);

            Users users = userOpt.orElseThrow(()->
            new NoSuchElementException(CommonConstants.USER_NOT_FOUND + userId)
            		);

            return new ResponseWrapper<>(HttpStatus.OK, CommonConstants.USER_FOUND_SUCCESSFULLY, true, userOpt.get());
        } catch (Exception e) {
            log.error("Error while fetching user by ID: {}", e.getMessage());
            return new ResponseWrapper<>(HttpStatus.INTERNAL_SERVER_ERROR, CommonConstants.UNEXPECTED_ERROR + e.getMessage() , false);
        }
    }

    /**
     * Retrieve a user by email.
     */
    public ResponseWrapper<Users> getUserByEmail(String email) {
        try {
            Optional<Users> userOpt = userRepository.findByEmail(email);

           Users users = userOpt.orElseThrow(()->
        		   new NoSuchElementException(CommonConstants.USER_NOT_FOUND + email)
        		   );

            return new ResponseWrapper<>(HttpStatus.OK, CommonConstants.USER_FOUND_SUCCESSFULLY, true, userOpt.get());
        } catch (Exception e) {
            log.error("Error while fetching user by email: {}", e.getMessage());
            return new ResponseWrapper<>(HttpStatus.INTERNAL_SERVER_ERROR, CommonConstants.UNEXPECTED_ERROR + e.getMessage(), false);
        }
    }

    /**
     * Update user by ID.
     */
    @Override
    public ResponseWrapper<Users> updateUser(Integer userId, UsersRequest usersRequest) {
        try {
            Optional<Users> existingUserOpt = userRepository.findById(userId);

            //forcing an exception 
            Users user = existingUserOpt.orElseThrow(()->
            new NoSuchElementException(CommonConstants.USER_NOT_FOUND +userId)
            		);

            Users existingUser = existingUserOpt.get();

            // Enable skipping null fields in ModelMapper
            modelMapper.getConfiguration().setSkipNullEnabled(true);

            // Map non-null fields
            modelMapper.map(usersRequest, existingUser);

            Users savedUser = userRepository.save(existingUser);

            emailService.sendUpdationMessage(savedUser.getEmail(), savedUser.getUserName());

            return new ResponseWrapper<>(HttpStatus.ACCEPTED, CommonConstants.USER_UPDATED_SUCCESSFULLY, true, savedUser);
        } catch (Exception e) {
            log.error("Error while updating user: {}", e.getMessage());
            return new ResponseWrapper<>(HttpStatus.INTERNAL_SERVER_ERROR, CommonConstants.UNEXPECTED_ERROR + e.getMessage(), false);
        }
    }

    /**
     * Soft delete user by ID.
     */
    
    /*
    @Override
	public ResponseWrapper<Void> deleteUser(Integer userId) {
    	
    	try {
		Optional<Users> optionalUser = userRepository.findByUserIdAndDeletedFalse(userId);

		
		if (optionalUser.isPresent()) {
			Users user = optionalUser.get();
			user.setDeleted(true); // Mark as deleted
			 userRepository.save(user); // Save the updated profile
		return new ResponseWrapper<>(HttpStatus.OK,CommonConstants.USER_DELETED_SUCCESSFULLY,true);
		} 
//		else {
//			return new ResponseWrapper<>(HttpStatus.NOT_FOUND,CommonConstants.USER_NOT_FOUND + userId,false);
//		}
    	}
    	catch (Exception e) {
    		return new ResponseWrapper<>(HttpStatus.INTERNAL_SERVER_ERROR,CommonConstants.UNEXPECTED_ERROR,false);
		}
		return null;
		
	}
*/
    @Override
    public ResponseWrapper<Void> deleteUser(Integer userId) {
        try {
            Optional<Users> optionalUser = userRepository.findByUserIdAndDeletedFalse(userId);

            // Force an exception if the user is not found
            Users user = optionalUser.orElseThrow(() -> 
                new NoSuchElementException(CommonConstants.USER_NOT_FOUND + userId)
            );

            // Mark as deleted and save
            user.setDeleted(true);
            userRepository.save(user);

            return new ResponseWrapper<>(HttpStatus.OK, CommonConstants.USER_DELETED_SUCCESSFULLY, true);

        } catch (Exception e) {
            // Catch block to handle both NoSuchElementException and other unexpected exceptions
            return new ResponseWrapper<>(HttpStatus.INTERNAL_SERVER_ERROR, CommonConstants.UNEXPECTED_ERROR  + e.getMessage(), false);
        }
    }


}
