package com.ve.task_management.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ve.task_management.constants.ResponseWrapper;
import com.ve.task_management.model.EmailRequest;
import com.ve.task_management.model.EmailSendingRequest;
import com.ve.task_management.service.EmailService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class EmailController {

	@Autowired
	private EmailService emailService;

//	private final EmailServiceImpl emailServiceImpl;	
//this one works

	@PostMapping("/send")
	public ResponseWrapper<String> send(@RequestParam String to, @RequestParam String subject,
			@RequestParam String message) throws MessagingException {
		try {
			emailService.sendEmail(to, subject, message);
			return new ResponseWrapper<>(HttpStatus.OK,"Email sent successfully",true);
		} catch (Exception e) {
			return new ResponseWrapper<>(HttpStatus.EXPECTATION_FAILED,"An unexpected error occured",false);
		}
	}

	/*
	 * @PostMapping("/sendEmail") public ResponseEntity<String>
	 * sendEmail(@RequestBody EmailRequest emailRequest) { if (emailRequest.getTo()
	 * == null || emailRequest.getTo().isEmpty()) { return
	 * ResponseEntity.badRequest().body("Recipient email is required!"); }
	 * emailService.sendEmailWithHtml(emailRequest.getTo(),
	 * emailRequest.getSubject(),emailRequest.getMessage()); return
	 * ResponseEntity.ok("Email sent!"); }
	 */

	@PostMapping("/sendEmailwithFile")
	public ResponseWrapper<Void> sendWithFile(@RequestBody EmailRequest emailrequest, @RequestParam MultipartFile file)
			throws IOException {
//		emailService.sendEmailWithFile(emailrequest.getTo(),emailrequest.getSubject(),emailrequest.getMessage(),file.getInputStream());
		return new ResponseWrapper<>(HttpStatus.ACCEPTED,"email sent successfully",true);
	}

}
