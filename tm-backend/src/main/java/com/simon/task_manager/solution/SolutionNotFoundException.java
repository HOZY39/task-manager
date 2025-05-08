package com.simon.task_manager.solution;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class SolutionNotFoundException extends  RuntimeException{
    public SolutionNotFoundException(){
        super("Solution Not Found");
    }
}
