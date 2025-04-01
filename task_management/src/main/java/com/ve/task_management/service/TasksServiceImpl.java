package com.ve.task_management.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ve.task_management.model.Tasks;
import com.ve.task_management.payload.TasksRequest;
import com.ve.task_management.payload.TasksResponse;
import com.ve.task_management.repository.TasksRepository;
@Service

public class TasksServiceImpl implements TasksService {
	@Autowired 
	TasksRepository taskRepository;
	@Autowired
	private ModelMapper modelMapper;

	
	@Override
	public List<TasksResponse> getAllTasks() {
		
		List<Tasks> list_tasks = taskRepository.findAll();

		List<TasksResponse> list_res = list_tasks.stream()
				.map(task -> modelMapper.map(task, TasksResponse.class))
				.collect(Collectors.toList());
		return list_res;
		
	}

	@Override
	public Tasks getTaskById(Integer taskId) {
		return taskRepository.findById(taskId)
				.orElseThrow(() -> new RuntimeException("task not found for ID: " + taskId));
	}
		
	

	@Override
	public Tasks createTasks(TasksRequest tasksRequest) {
		Tasks task = modelMapper.map(tasksRequest, Tasks.class);
	    return taskRepository.save(task);
	}

	@Override
	public void updateTask(Integer taskId, TasksRequest tasksRequest) {
		Tasks existingTasks = taskRepository.findById(taskId)
		        .orElseThrow(() -> new RuntimeException("task not found with id: " + taskId));
		    
		    // Use ModelMapper to map updated fields from the request to the existing entity
		    modelMapper.map(tasksRequest, existingTasks);

		    
		    // Save the updated entity
		    taskRepository.save(existingTasks);
		
		
	}

	@Override
	public void deleteTaskById(Integer taskId) {
		 Optional<Tasks> optionalTask = taskRepository.findByTaskIdAndDeletedFalse(taskId);

	     if (optionalTask.isPresent()) {
	         Tasks task = optionalTask.get();
	       task.setDeleted(true); // Mark as deleted
	         taskRepository.save(task); // Save the updated profile
	     } else {
	         throw new RuntimeException("task not found or already deleted for Project ID: " + taskId);}
		

		
	}
	
	
}
