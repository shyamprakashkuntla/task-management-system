package com.ve.task_management.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class Users implements Serializable {
	
	private static final long serialVersionUID = 1L;   // Required for version control
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Integer userId;

	@Column(name = "email", unique = true, nullable = false)
    private String email;
	
	@Column(name = "userName")
	private String userName;

	@Column(name = "password")
	private String password;

	@Column(name = "remarks")
	private String remarks;

	@Column(name = "user_role")
	private String userRole;

	@Column(nullable = false)
	private boolean deleted = false;
	
	
	@Column(name = "failed_login_attempts", nullable = false)
    private int failedLoginAttempts = 0;   // Track login failures

    @Column(name = "lockout_time")
    private LocalDateTime lockoutTime;     // Time of lockout

    @Column(name = "is_account_locked", nullable = false)
    private boolean isAccountLocked = false;  // Account lock status

	
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id")
	private List<Allocation> allocation;
	
	
	/*
	 @OneToMany(mappedBy = "users", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	 private List<Allocation> allocation;
	 */
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id")
	private List<Clients> clients;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id")
	private List<Projects> projects;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id")
	private List<Tasks> task;

	@ManyToOne
	@JoinColumn(name = "admin_user_id")
	private Users adminUsers;

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id")
	private ForgotPassword forgotPassword;

}
