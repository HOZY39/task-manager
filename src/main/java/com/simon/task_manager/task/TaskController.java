package com.simon.task_manager.task;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import com.simon.task_manager.solution.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskRepository taskRepository;
    private final SolutionRepository solutionRepository;

    private static final Logger log = LoggerFactory.getLogger(TaskController.class);

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

    @GetMapping("/section/{section}")
    List<Task> findAllBySection(@PathVariable String section) {
        return taskRepository.findAllBySection(section);
    }
    
    @GetMapping("/search/{search}")
    List<Task> findAllByText(@PathVariable String search) {
        return taskRepository.findAllByText(search);
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
    public int create(@RequestBody Task task){
        return taskRepository.create(task);
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

    @PostMapping("/{id}/image")
    public ResponseEntity<String> uploadImage(@PathVariable Integer id, @RequestParam("image") MultipartFile file) {
        try {
            taskRepository.addImage(file, id);
            return ResponseEntity.ok("Image uploaded successfully.");
        } catch (IOException e) {
            log.error("Failed to upload image", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload image");
        }
    }
}
