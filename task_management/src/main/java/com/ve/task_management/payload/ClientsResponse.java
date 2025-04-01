package com.ve.task_management.payload;


import lombok.Data;

@Data
public class ClientsResponse {
private Integer clientId;
	
	private String  clientName;
	private String  address;
	
	private String  city;
	private String  pincode;
	
	private String  contactPerson;
	
	private String  contactNumber;
	
	private String  contactEmailId;

	private String  remarks;

	
}
