package com.ve.task_management.service;

import java.util.List;

import com.ve.task_management.constants.ResponseWrapper;
import com.ve.task_management.model.Users;
import com.ve.task_management.payload.UsersRequest;
import com.ve.task_management.payload.UsersResponse;

public interface UsersService {

	ResponseWrapper<List<UsersResponse>> getAllUsers();
	ResponseWrapper<Users> getUserById(Integer userId);
	ResponseWrapper<Users> createUser(UsersRequest user );
	ResponseWrapper<Users> updateUser(Integer userId, UsersRequest usersRequest);
	ResponseWrapper<Void> deleteUser(Integer userId);
	ResponseWrapper<Users> getUserByEmail(String email);
	
	
	
}
