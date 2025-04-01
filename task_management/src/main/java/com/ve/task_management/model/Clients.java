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
@Table(name="clients")
@Data
public class Clients implements Serializable {
	
	private static final long serialVersionUID = 1L;   // Required for version control
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "client_id")
	private Integer clientId;
	
	@Column(name = "client_name")
	private String  clientName;
	
	@Column(name = "addresss")
	private String  address;
	
	@Column(name = "city")
	private String  city;
	
	@Column(name = "pincode")
	private String  pincode;
	
	@Column(name = "contact_person")
	private String  contactPerson;
	
	@Column(name = "contact_number")
	private String  contactNumber;
	
	@Column(name = "contact_email_id")
	private String  contactEmailId;
	
	@Column(name = "remarks")
	private String  remarks;
	
	@Column(nullable = false)
    private boolean deleted = false;

	 @ManyToOne
	 @JoinColumn(name ="user_id")
	 @JsonIgnore
	 private Users users;
	

	
}

