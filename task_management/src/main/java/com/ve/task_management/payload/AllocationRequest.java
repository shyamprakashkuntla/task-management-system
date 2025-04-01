package com.ve.task_management.payload;

import lombok.Data;

@Data
public class AllocationRequest {
	//form bean class is used in the place of payload.
//private Integer allocationId;
	
	private String project;
	
	private String user;
	 
}
