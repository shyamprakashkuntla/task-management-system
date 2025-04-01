package com.ve.task_management.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.tags.Tag;

@Configuration
public class Swaggerconfiguration {

	@Bean
	public OpenAPI customConfig()
	{
		return new OpenAPI().info(
				
				new Info().title("VALUEDGE SOLUTIONS")
				.description("BY valuedge backend development")
				)
				.tags(Arrays.asList(
						new Tag().name("USER CONTROLLER").description("manages user operation"),
						new Tag().name("CLIENT CONTROLLER"),
						new Tag().name("PROJECT CONTROLLER"),
						new Tag().name("TASK CONTROLLER"),
						new Tag().name("ALLOCATION CONTROLLER")
						));
				
	}
}
