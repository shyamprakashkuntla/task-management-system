package com.ve.task_management.controller;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ve.task_management.constants.CommonConstants;
import com.ve.task_management.constants.ResponseWrapper;
import com.ve.task_management.model.ChangePasswordRequest;
import com.ve.task_management.model.EmailRequest;
import com.ve.task_management.model.ForgotPassword;
import com.ve.task_management.model.MailBody;
import com.ve.task_management.model.Otprequest;
import com.ve.task_management.model.Users;
import com.ve.task_management.repository.ForgotPasswordRepository;
import com.ve.task_management.repository.UsersRepository;
import com.ve.task_management.service.EmailService;

@RestController
@RequestMapping("/forgotPassword")
@CrossOrigin(origins = "*")
public class FogotpasswordController {

    private final UsersRepository userRepository;
    private final EmailService emailService;
    private final ForgotPasswordRepository forgotPasswordRepository;
    private final PasswordEncoder passwordEncoder;

    public FogotpasswordController(UsersRepository userRepository, EmailService emailService, 
                                   ForgotPasswordRepository forgotPasswordRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.forgotPasswordRepository = forgotPasswordRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ✅ Send email verification with OTP (using request body)
    @PostMapping("/verifyMail")
    public ResponseWrapper<String> verifyEmail(@RequestBody EmailRequest request) {
        Users user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Invalid email: " + request.getEmail()));

        int otp = otpGenerator();
        MailBody mailBody = MailBody.builder()
                .to(request.getEmail())
                .text("Your OTP for password reset: " + otp)
                .subject("OTP for Password Reset")
                .build();

        ForgotPassword fp = ForgotPassword.builder()
                .otp(otp)
                .expirationTime(new Date(System.currentTimeMillis() + 5 * 60 * 1000)) // 5 minutes expiry
                .users(user)
                .build();

        emailService.sendSimpleMessage(mailBody);
        forgotPasswordRepository.save(fp);

        String data = "please check your email for otp, check the email again if the otp is not sent";
        
        return new ResponseWrapper(HttpStatus.ACCEPTED,CommonConstants.OTP_SENT,true,data);
    }

    // ✅ Verify OTP (using request body)
    @PostMapping("/verifyOtp")
    public ResponseWrapper<String> verifyOtp(@RequestBody Otprequest request) {
        Users user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Invalid email: " + request.getEmail()));

        ForgotPassword fp = forgotPasswordRepository.findByOtpAndUsers(request.getOtp(), user)
                .orElseThrow(() -> new RuntimeException("Invalid OTP for email: " + request.getEmail()));

        if (fp.getExpirationTime().before(Date.from(Instant.now()))) {
            forgotPasswordRepository.deleteById(fp.getFpid());
            return new ResponseWrapper<>( HttpStatus.EXPECTATION_FAILED,"OTP has expired!",false);
        }

        return new ResponseWrapper<>(HttpStatus.ACCEPTED,"otp verified successfully",true);
    }

    // ✅ Change Password (using request body)
    @PostMapping("/changePassword")
    public ResponseWrapper<String> changePasswordHandler(@RequestBody ChangePasswordRequest request) {

        if (!Objects.equals(request.getNewPassword(), request.getRepeatPassword())) {
            return new ResponseWrapper<>( HttpStatus.EXPECTATION_FAILED,"Passwords do not match try changing password agian!",false);
        }

        Users user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Invalid email: " + request.getEmail()));

        String encodedPassword = passwordEncoder.encode(request.getNewPassword());
        userRepository.updatePassword(request.getEmail(), encodedPassword);

        return new ResponseWrapper<>(HttpStatus.ACCEPTED,"Password has been changed successfully.",true);
    }

    // OTP Generator
    private Integer otpGenerator() {
        Random random = new Random();
        return random.nextInt(100_000, 999_999);
    }
}
