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
@Table(name="projects")
@Data
public class Projects implements Serializable {
	
	private static final long serialVersionUID = 1L;   // Required for version control
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "project_id")
	private Integer projectId;
	
	@Column(name = "project_name")
	private String projectName;
	
	@Column(name = "client_name")
	private String  clientName;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "start_date")
	private LocalDateTime startDate;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "end_date")
	private LocalDateTime endDate;
	
	@Column(name = "remarks")
	private String remarks;
	
	@Column(nullable = false)
    private boolean deleted = false;
	
	
	 @ManyToOne
	 @JoinColumn(name ="user_id")
	 @JsonIgnore
	 private Users users;

}
