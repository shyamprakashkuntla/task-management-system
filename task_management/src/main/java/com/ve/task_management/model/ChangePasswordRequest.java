package com.ve.task_management.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordRequest {
    private String email;
    private String newPassword;
    private String repeatPassword;
}