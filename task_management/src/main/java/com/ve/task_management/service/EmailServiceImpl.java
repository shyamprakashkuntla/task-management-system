package com.ve.task_management.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.ve.task_management.model.MailBody;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {

	private final JavaMailSender javaMailSender;

	public EmailServiceImpl(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}

	private Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

	/*
	//sending one mail
	@Override
	public void sendEmail(String to, String subject, String message) {
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(to);
		mailMessage.setSubject(subject);
		mailMessage.setText(message);
		mailMessage.setFrom("shyamprakash.kuntla@gmail.com");
		javaMailSender.send(mailMessage);
		logger.info("email has been sent sucessfully");
	}
	*/
	@Override
	 public void sendEmail(String to, String subject, String message) throws MessagingException {
	        // Create a new MimeMessage
	        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

	        // Create a helper object to send the email
	        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
	        helper.setTo(to);
	        helper.setSubject(subject);
	        helper.setText(message);

	        // Send the email
	        javaMailSender.send(mimeMessage);
	    }

	//sending list of mails
	@Override
	public void sendEmail(String[] to, String subject, String message) throws MessagingException {
		// Create a new MimeMessage
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        // Create a helper object to send the email
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(message);

        // Send the email
        javaMailSender.send(mimeMessage);
		logger.info("email has been sent sucessfully");
	}

	//sending with html
	@Override
	public void sendEmailWithHtml(String to, String subject, String htmlContent) {
		MimeMessage mailMessage = javaMailSender.createMimeMessage();

		try {
			MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage, true, "UTF-8");
			messageHelper.setTo(to);
			messageHelper.setSubject(subject);
			messageHelper.setText(htmlContent, true);
			messageHelper.setFrom("shyamprakash.kuntla@gmail.com");

			javaMailSender.send(mailMessage);
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}

	}

	//sending with file
	@Override
	public void sendEmailWithFile(String to, String subject, String message, File file) {
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		try {
			MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
			messageHelper.setTo(to);
			messageHelper.setSubject(subject);
			messageHelper.setFrom("shyamprakash.kuntla@gmail.com");
			messageHelper.setText(message);
			FileSystemResource fileSystemResource = new FileSystemResource(file);
			messageHelper.addAttachment(fileSystemResource.getFilename(), file);

			javaMailSender.send(mimeMessage);
			logger.info("message sent successfully");
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

	//copying the file in the local machine and pasting to the specific location mentioned
	@Override
	public void sendEmailWithFile(String to, String subject, String message, InputStream is) {
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		try {
			MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
			messageHelper.setTo(to);
			messageHelper.setSubject(subject);
			messageHelper.setFrom("shyamprakash.kuntla@gmail.com");
			messageHelper.setText(message);

			File file = new File("src/main/resources/email/test.png");
			Files.copy(is, file.toPath(), StandardCopyOption.REPLACE_EXISTING);

			FileSystemResource fileSystemResource = new FileSystemResource(file);
			messageHelper.addAttachment(fileSystemResource.getFilename(), file);

			javaMailSender.send(mimeMessage);
			logger.info("message sent successfully");
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	//  used to implement forgot password functionality

	public void sendSimpleMessage(MailBody mailBody) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(mailBody.to());
		message.setFrom("shyamprakash.kuntla@gmail.com");
		message.setSubject(mailBody.subject());
		message.setText(mailBody.text());

		javaMailSender.send(message);
	}
	
	//send message after registration
	
	public void sendRegistrationEmail(String toEmail, String username) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("shyamprakash.kuntla@gmail.com");
            helper.setTo(toEmail);
            helper.setSubject("Registration Successful");

            String content = "<p>Hello <b>" + username + "</b>,</p>"
                    + "<p>Welcome! You have successfully registered.</p>"
                    + "<p>Thank you for joining us.</p>"
                    + "<p>Best Regards,<br>Task Management Team</p>";

            helper.setText(content, true);

            javaMailSender.send(message);
            System.out.println("Confirmation email sent to: " + toEmail);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

	@Override
	public void sendUpdationMessage(String toEmail, String username) {
		try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("shyamprakash.kuntla@gmail.com");
            helper.setTo(toEmail);
            helper.setSubject("Attention user, contents updated");

            String content = "<p>Hello <b>" + username + "</b>,</p>"
                    + "<p>some contents were updated on your id.</p>"
                    + "<p>if not you, please change the password for security.</p>"
                    + "<p>Best Regards,<br>Task Management Team</p>";

            helper.setText(content, true);

            javaMailSender.send(message);
            System.out.println("updated email sent to: " + toEmail);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
		
	}
}
