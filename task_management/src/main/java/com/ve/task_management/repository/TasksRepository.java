package com.ve.task_management.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ve.task_management.model.Tasks;

public interface TasksRepository extends JpaRepository<Tasks,Integer>{

	Optional<Tasks> findByTaskIdAndDeletedFalse(Integer taskId);

}
