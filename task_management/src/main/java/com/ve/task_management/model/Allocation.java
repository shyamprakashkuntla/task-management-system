package com.ve.task_management.model;

import java.io.Serializable;

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
@Table(name="allocation")
@Data

public class Allocation implements Serializable {
	
	private static final long serialVersionUID = 1L;   // Required for version control
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "allocation_id")
	private Integer allocationId;
	@Column(name = "project")
	private String project;
	@Column(name = "user")
	private String user;
	//in client side data will be present.
	//in co-ordination of delete mapping.
	 @Column(nullable = false)
	 private boolean deleted = false;
	 
	 
	 @ManyToOne
	 @JoinColumn(name ="user_id")
	 @JsonIgnore
	 private Users users;
	 
	 
	 /*
	 @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "user_id")
	    @JsonIgnore
	    private Users users;
	    */
}
