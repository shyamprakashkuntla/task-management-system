package com.ve.task_management.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ve.task_management.model.Projects;
import com.ve.task_management.payload.ProjectsRequest;
import com.ve.task_management.payload.ProjectsResponse;
import com.ve.task_management.repository.ProjectsRepository;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    ProjectsRepository projectRepository;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public List<ProjectsResponse> getAllProjects() {
        List<Projects> list_projects = projectRepository.findAll();

        return list_projects.stream()
                .map(project -> modelMapper.map(project, ProjectsResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public Projects getProjectById(Integer projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found for ID: " + projectId));
    }

    @Override
    public Projects createProject(ProjectsRequest projectsRequest) {

        Projects project = modelMapper.map(projectsRequest, Projects.class);

        return projectRepository.save(project);
    }

    @Override
    public void updateProject(Integer projectId, ProjectsRequest projectsRequest) {
        Projects existingProject = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + projectId));

        // Map updated fields from DTO to entity
        modelMapper.map(projectsRequest, existingProject);

        projectRepository.save(existingProject);
    }

    @Override
    public void deleteProjectById(Integer projectId) {
        Optional<Projects> optionalProject = projectRepository.findByProjectIdAndDeletedFalse(projectId);

        if (optionalProject.isPresent()) {
            Projects project = optionalProject.get();
            project.setDeleted(true);
            projectRepository.save(project);
        } else {
            throw new RuntimeException("Project not found or already deleted for ID: " + projectId);
        }
    }

   
}
