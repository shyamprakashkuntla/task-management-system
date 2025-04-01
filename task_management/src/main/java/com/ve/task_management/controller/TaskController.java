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
import com.ve.task_management.model.Tasks;
import com.ve.task_management.payload.TasksRequest;
import com.ve.task_management.payload.TasksResponse;
import com.ve.task_management.service.TasksService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@Tag(name="TASK CONTROLLER",description ="Get,update,Read all,Read by ID,Delete by Id")
public class TaskController {
	@Autowired
	TasksService taskService;
	@Autowired 
	private ModelMapper modelMapper;
	@GetMapping("/getAllTasks")
    public ResponseWrapper<List<TasksResponse>> getAllTasks() {
        List<TasksResponse> taskList = taskService.getAllTasks();
        return new ResponseWrapper<>(HttpStatus.OK, "tasks fetched successfully", true, taskList);
    }

    @PostMapping("/saveTasks")
    public ResponseWrapper<Void> createTask(@Valid @RequestBody TasksRequest tasksRequest) {
        taskService.createTasks(tasksRequest);
        return new ResponseWrapper<>(HttpStatus.OK, CommonConstants.TASK_SUCCESSFULLY, true);
    }

 
    @GetMapping("/Task/{taskId}")
    public ResponseWrapper<TasksResponse> getTasksById(@PathVariable Integer taskId) {
        Tasks task = taskService.getTaskById(taskId);

        // Use ModelMapper to map the entity to the response DTO
        TasksResponse response = modelMapper.map(task, TasksResponse.class);

        return new ResponseWrapper<>(HttpStatus.OK, "Task fetched successfully", true, response);
    }
    @DeleteMapping("/deleteTask/{taskId}")
    public ResponseWrapper<Void> deleteTask(@PathVariable Integer taskId) {
        taskService.deleteTaskById(taskId);
        return new ResponseWrapper<>(HttpStatus.OK, CommonConstants.TASK_DELETED_SUCCESSFULLY, true);
    }

    @PutMapping("/updateTask/{taskId}")
    public ResponseWrapper<Void> updateTask(
            @PathVariable Integer taskId,
            @Valid @RequestBody TasksRequest tasksRequest) {
        taskService.updateTask(taskId, tasksRequest);
        return new ResponseWrapper<>(HttpStatus.OK, CommonConstants.TASK_UPDATED_SUCCESSFULLY, true);
    }
	

}
