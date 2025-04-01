package com.ve.task_management.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.ve.task_management.model.Users;

import jakarta.transaction.Transactional;

public interface UsersRepository extends JpaRepository<Users,Integer> {

	Optional<Users> findByUserIdAndDeletedFalse(Integer userId);

	Optional<Users> findByEmail(String email);
	
	Optional<Users> findByEmailAndIsAccountLockedFalse(String email);

//	Optional<Users> findByUserName(String email); // userName is the email

    @Transactional
    @Modifying
    @Query("update Users u set u.password = ?2 where u.email = ?1")
    void updatePassword(String email, String password);
    
    

}
