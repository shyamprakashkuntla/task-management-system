package com.ve.task_management.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ve.task_management.model.Projects;

public interface ProjectsRepository extends JpaRepository<Projects,Integer>{

	Optional<Projects> findByProjectIdAndDeletedFalse(Integer projectId);

}
