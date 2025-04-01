package com.ve.task_management.payload;

import lombok.Data;

@Data
public class Loginrequest {
	private String email;
    private String password;
}
