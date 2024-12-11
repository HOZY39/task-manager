package com.simon.task_manager.task;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import com.simon.task_manager.solution.*;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskRepository taskRepository;
    private final SolutionRepository solutionRepository;

    public TaskController(TaskRepository taskRepository, SolutionRepository solutionRepository) {
        this.taskRepository = taskRepository;
        this.solutionRepository = solutionRepository;
    }

    @GetMapping
    List<Task> findAll() {
        return taskRepository.findAll();
    }

    @GetMapping("/{id}/solutions")
    List<Solution> findAllSol(@PathVariable Integer id) {
        return solutionRepository.findAllForTask(id);
    }
    

     @GetMapping("/{id}")
     Task findById(@PathVariable Integer id) {
         Optional<Task> task = taskRepository.findById(id);
         if (task.isPresent()){
             return task.get();
         }else{
             throw new TaskNotFoundException();
         }
     }

     @ResponseStatus(HttpStatus.CREATED)
     @PostMapping("")
     void create(@RequestBody Task task){
         taskRepository.create(task);
     }

     @ResponseStatus(HttpStatus.NO_CONTENT)
     @PutMapping("/{id}")
     void update(@RequestBody Task task, @PathVariable Integer id){
         taskRepository.update(task, id);
     }

     @ResponseStatus(HttpStatus.NO_CONTENT)
     @DeleteMapping("/{id}")
     void delete(@PathVariable Integer id){
         taskRepository.delete(id);
     }
}
