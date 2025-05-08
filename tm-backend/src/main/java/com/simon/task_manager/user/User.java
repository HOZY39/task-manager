package com.simon.task_manager.user;

public record User (
        String username,
        String email,
        String password,
        String role
){
}
