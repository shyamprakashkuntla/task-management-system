package com.ve.task_management.payload;

import java.util.List;

import lombok.Data;

@Data
public class UsersResponse {

	private Integer userId;

    private String email;
    
	private String userName;

	private String password;
	
	private String remarks;
	
	private String userRole;

	List<AllocationResponse> allocation;
	List<ClientsResponse> clients;
	List<ProjectsResponse> projects;
	List<TasksResponse> task;

}
