package com.ve.task_management.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ve.task_management.model.ForgotPassword;
import com.ve.task_management.model.Users;

public interface ForgotPasswordRepository extends JpaRepository<ForgotPassword, Integer> {

	 @Query("select fp from ForgotPassword fp where fp.otp = ?1 and fp.users = ?2")
	    Optional<ForgotPassword> findByOtpAndUsers(Integer otp, Users user);
}
