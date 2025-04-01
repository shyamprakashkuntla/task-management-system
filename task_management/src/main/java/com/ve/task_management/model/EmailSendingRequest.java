package com.ve.task_management.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailSendingRequest {

	@NotBlank(message = "The 'to' address must not be null or empty.")
    @Email(message = "The 'to' address must be a valid email.")
    private String to;

    @NotBlank(message = "The 'subject' must not be null or empty.")
    private String subject;

    @NotBlank(message = "The 'message' must not be null or empty.")
    private String message;
}
