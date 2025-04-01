package com.ve.task_management.service;

import java.io.File;
import java.io.InputStream;

import com.ve.task_management.model.MailBody;

import jakarta.mail.MessagingException;

public interface EmailService {

	//send email to single person
	void sendEmail(String to,String subject,String message ) throws MessagingException;
	//send email to multiple person
	void sendEmail(String[] to,String subject,String message)throws MessagingException;
	//send email with html
	void sendEmailWithHtml(String to,String subject,String htmlContent);
	//send email with file
	void sendEmailWithFile(String to,String subject,String message,File file);
	//with input stream
	void sendEmailWithFile(String to,String subject,String message,InputStream is);
	
	 void sendRegistrationEmail(String toEmail, String username);
	void sendSimpleMessage(MailBody mailBody);
	
	void sendUpdationMessage(String toEmail,String username);
}
