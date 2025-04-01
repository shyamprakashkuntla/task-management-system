package com.ve.task_management.config;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class SecurityConfigProperties {

	private String secretKey;
	private Long jwtTtl;
	
	
}
