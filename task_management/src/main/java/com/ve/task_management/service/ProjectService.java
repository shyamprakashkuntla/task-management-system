package com.ve.task_management.service;

import java.util.List;

import com.ve.task_management.model.Projects;
import com.ve.task_management.payload.ProjectsRequest;
import com.ve.task_management.payload.ProjectsResponse;

public interface ProjectService {
  List<ProjectsResponse> getAllProjects();
  Projects getProjectById(Integer projectId);
  Projects createProject(ProjectsRequest  projectsRequest);
  void updateProject( Integer projectId ,ProjectsRequest  projectsRequest);
  void deleteProjectById(Integer projectId);
 
	
	
	
}
