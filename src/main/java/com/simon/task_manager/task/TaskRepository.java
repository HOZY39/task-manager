package com.simon.task_manager.task;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import jakarta.annotation.PostConstruct;



@Repository
public class TaskRepository {

    private List<Task> tasks = new ArrayList<>();

    List<Task> findAll() {
        return tasks;
    }

    Optional<Task> findById(Integer id){
        return tasks.stream()
            .filter(task -> task.id() == id)
            .findFirst();
    }

    void create(Task task) {
        tasks.add(task);
    }

    void update(Task task, Integer id) {
        Optional<Task> taskToUpdate = findById(id);
        if (taskToUpdate.isPresent()) {
            tasks.set(tasks.indexOf(taskToUpdate.get()), task);
        }
    }

    void delete(Integer id){
        tasks.removeIf(task -> task.id().equals(id));
    }

    @PostConstruct
    private void init() {
        tasks.add(new Task(1, subject.MATH, "Addition", 
        "I have 5 apples, my mom gave me another 3. How many apples do I have?", 
        LocalDateTime.now()));

        tasks.add(new Task(2, subject.MATH, "Derivative", 
        "What is the derivative of x squared?", 
        LocalDateTime.now()));

        tasks.add(new Task(3, subject.PHYSICS, "Force", 
        "Ball wieghs 3kg. What is the force of gravity?", 
        LocalDateTime.now()));
    }
}
