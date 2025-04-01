package com.ve.task_management.controller;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ve.task_management.constants.CommonConstants;
import com.ve.task_management.constants.ResponseWrapper;
import com.ve.task_management.model.Projects;
import com.ve.task_management.payload.ProjectsRequest;
import com.ve.task_management.payload.ProjectsResponse;
import com.ve.task_management.service.ProjectService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@Tag(name="PROJECT CONTROLLER",description ="Get,update,Read all,Read by ID,Delete by Id")
public class ProjectController {
	@Autowired 
	private ModelMapper modelMapper;
	@Autowired 
	ProjectService projectService;
	 @GetMapping("/getAllProjects")
	    public ResponseWrapper<List<ProjectsResponse>> getAllProjects() {
	        List<ProjectsResponse> projectList = projectService.getAllProjects();
	        return new ResponseWrapper<>(HttpStatus.OK, "Projects fetched successfully", true, projectList);
	    }

	    @PostMapping("/saveProjects")
	    public ResponseWrapper<Void> createProject(@Valid @RequestBody ProjectsRequest projectRequest) {
	        projectService.createProject(projectRequest);
	        return new ResponseWrapper<>(HttpStatus.OK, CommonConstants.PROJECTS_SUCCESSFULLY, true);
	    }

	 
	    @GetMapping("/Projects/{projectId}")
	    public ResponseWrapper<ProjectsResponse> getProjectById(@PathVariable Integer projectId) {
	        Projects pr = projectService.getProjectById(projectId);

	        // Use ModelMapper to map the entity to the response DTO
	        ProjectsResponse response = modelMapper.map(pr, ProjectsResponse.class);

	        return new ResponseWrapper<>(HttpStatus.OK, "Project fetched successfully", true, response);
	    }
	    @DeleteMapping("/deleteProject/{projectId}")
	    public ResponseWrapper<Void> deleteProject(@PathVariable Integer projectId) {
	        projectService.deleteProjectById(projectId);
	        return new ResponseWrapper<>(HttpStatus.OK, CommonConstants.PROJECT_DELETED_SUCCESSFULLY, true);
	    }

	    @PutMapping("/updateProject/{projectId}")
	    public ResponseWrapper<Void> updateProject(
	            @PathVariable Integer projectId,
	            @Valid @RequestBody ProjectsRequest projectsRequest) {
	        projectService.updateProject(projectId, projectsRequest);
	        return new ResponseWrapper<>(HttpStatus.OK, CommonConstants.PROJECT_UPDATED_SUCCESSFULLY, true);
	    }


}
