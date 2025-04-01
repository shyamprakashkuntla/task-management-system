package com.ve.task_management;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TaskManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskManagementApplication.class, args);
	}
	@Bean
    public ModelMapper modelMapper() {
		 ModelMapper mapper = new ModelMapper();
	        // Skip null values during mapping
	        mapper.getConfiguration().setPropertyCondition(context -> context.getSource() != null);
	        return mapper;
    }
}
