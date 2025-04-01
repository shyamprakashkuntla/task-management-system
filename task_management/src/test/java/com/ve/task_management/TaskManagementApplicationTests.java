package com.ve.task_management;

import java.io.File;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ve.task_management.service.EmailService;

@SpringBootTest
class TaskManagementApplicationTests {

	@Autowired
	private EmailService emailService;
	
	/*
	public TaskManagementApplicationTests(EmailService emailService) {
		this.emailService = emailService;
	}
	*/

	@Test
	void contextLoads() 
	{
		System.out.println("sending mail");
//		emailService.sendEmail("demome848@gmail.com", "email from springboot", "sent using spring boot mailing");
	}

//	@Test
	void sendHtmlEmail()
	{
		System.out.println("sending email using html");
		String html = ""+"<h1 style='color:red;border:solid 1px red;'>this is demo of html for ravi(DX) bro. thanks for recieving</h1>"+"";
		emailService.sendEmailWithHtml("dxravi55@gmail.com", "email from spring boot", html);
	}
//	@Test
	void sendFIle()
	{
		System.out.println("sending email");
		emailService.sendEmailWithFile("demome848@gmail.com", "email with file",
				"this email contains file",
				new File("E:\\agate solutions\\project3\\task_management\\src\\main\\resources\\static\\images\\b9c96e1a-294e-45c2-aea2-ee38d7866c20.jpg"));
	}
	
}
