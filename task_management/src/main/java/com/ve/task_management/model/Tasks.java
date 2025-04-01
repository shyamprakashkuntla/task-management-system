package com.ve.task_management.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
@Entity
@Table(name="tasks")
@Data
public class Tasks implements Serializable{
	
	private static final long serialVersionUID = 1L;   // Required for version control
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "task_id")
	private Integer taskId;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//	@JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "date")
	private LocalDateTime date;
	
	@Column(name = "project_name")
	private String project_name;
	
	@Column(name = "task_name")
	private String task_name;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "remarks")
	private String remarks;
	
	@Column(nullable = false)
    private boolean deleted = false;
	
	 @ManyToOne
	 @JoinColumn(name ="user_id")
	 @JsonIgnore
	 private Users users;
	
	 
	 
}
