package com.ve.task_management.service;


import java.util.List;

import com.ve.task_management.model.Tasks;
import com.ve.task_management.payload.TasksRequest;
import com.ve.task_management.payload.TasksResponse;

public interface TasksService {
List<TasksResponse> getAllTasks();
 Tasks  getTaskById(Integer taskId);
 Tasks createTasks(TasksRequest tasksRequest);
 void  updateTask(Integer taskId ,TasksRequest tasksRequest);
 void deleteTaskById(Integer taskId);
 
 


}
