package com.ve.task_management.payload;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
@Data
public class TasksRequest {
	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime date;
	
	private String project_name;
	
	private String task_name;
	
	private String status;
	
	private String remarks;
	
}
