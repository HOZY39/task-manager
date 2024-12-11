package com.simon.task_manager.user;

public record User (
        Integer id,
        String username,
        String email,
        String password,
        role role
){
}
