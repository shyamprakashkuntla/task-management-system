package com.ve.task_management.payload;


import java.util.List;

import lombok.Data;

@Data
public class UsersRequest {
	
//	private Integer userId;
	
	private String userName;
	
	private String password;
	
	private String email;
	
	private String remarks;
	
	private String userRole;
    
    List<AllocationRequest> allocation;
    List<ClientsRequest> clients;
    List<ProjectsRequest> projects;
    List<TasksRequest> task;
}
