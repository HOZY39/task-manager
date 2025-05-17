package com.simon.task_manager.task;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import com.simon.task_manager.solution.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;
    private final SolutionRepository solutionRepository;

    public TaskController(TaskService taskService, SolutionRepository solutionRepository){
        this.taskService = taskService;
        this.solutionRepository = solutionRepository;
    }

    @GetMapping
    public List<Task> findAll() {
        return taskService.findAll();
    }

    @GetMapping("/{id}")
    public Task findById(@PathVariable Integer id) {
        Optional<Task> task = taskService.findById(id);
        if (task.isPresent()) {
            return task.get();
        } else {
            throw new TaskNotFoundException();
        }
    }

    @GetMapping("/section/{section}")
    public List<Task> findAllBySection(@PathVariable String section) {
        return taskService.findAllBySection(section);
    }

    @GetMapping("/search/{search}")
    public List<Task> findAllByText(@PathVariable String search) {
        return taskService.findAllByText(search);
    }

    @GetMapping("/{id}/solutions")
    public List<Solution> findAllSolutionsForTask(@PathVariable Integer id) {
        return solutionRepository.findAllForTask(id);
    }

    @PostMapping
    public ResponseEntity<Integer> create(@RequestBody Task task) {
        Integer id = taskService.create(task);
        return ResponseEntity.ok(id);
    }

    @PostMapping("/{id}/image")
    public ResponseEntity<Void> uploadImage(@PathVariable Integer id, @RequestParam MultipartFile image) throws IOException {
        taskService.addImage(image, id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/description")
    public ResponseEntity<Void> updateDescription(@PathVariable Integer id, @RequestBody String newDescription) {
        taskService.updateDescription(id, newDescription);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        taskService.deleteTask(id);
        return ResponseEntity.ok().build();
    }
}

