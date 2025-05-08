package com.simon.task_manager.task;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import com.simon.task_manager.solution.SolutionRepository;

@Service
public class TaskService {

    private final SolutionRepository solutionRepository;
    private final TaskRepository taskRepository;

    public TaskService(SolutionRepository solutionRepository, TaskRepository taskRepository) {
        this.solutionRepository = solutionRepository;
        this.taskRepository = taskRepository;

    }

    @Value("${file.upload-dir-img}")
    private String uploadDir;

    private static final Logger log = LoggerFactory.getLogger(TaskService.class);

    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    public List<Task> findAllBySection(String section) {
        return taskRepository.findAllBySection(section);
    }

    public List<Task> findAllByText(String searchText) {
        return taskRepository.findAllByText(searchText);
    }

    public Optional<Task> findById(Integer id) {
        return taskRepository.findById(id);
    }

    @Transactional
    public Integer create(Task task) {
        return taskRepository.create(task);
    }

    @Transactional
    public void addImage(MultipartFile file, Integer taskId) throws IOException {
        File dir = new File(uploadDir);
        if (!dir.exists() && !dir.mkdirs()) {
            throw new RuntimeException("Cannot create directory: " + uploadDir);
        }

        String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path path = Paths.get(uploadDir, filename);
        Files.write(path, file.getBytes());

        try {
            int updated = taskRepository.insertImage(taskId, filename);
            Assert.state(updated == 1, "Failed to insert image metadata");
        } catch (Exception e) {
            Files.deleteIfExists(path); // rollback manual file write
            throw e;
        }
    }

    @Transactional
    public void updateDescription(Integer taskId, String newDescription) {
        int updated = taskRepository.updateDescription(taskId, newDescription);
        Assert.state(updated == 1, "Failed to update description");
    }

    @Transactional
    public void deleteTask(Integer taskId) {
        // Delete associated images from disk
        for (String imageUrl : taskRepository.findImageUrls(taskId)) {
            try {
                Files.deleteIfExists(Paths.get(uploadDir, imageUrl));
            } catch (IOException e) {
                log.warn("Could not delete image: {}", imageUrl, e);
            }
        }

        solutionRepository.findAllForTask(taskId).forEach(solution -> {
            solutionRepository.delete(solution.id());
        });

        taskRepository.deleteImagesByTaskId(taskId);
        int deleted = taskRepository.deleteTask(taskId);
        Assert.state(deleted == 1, "Failed to delete task");
    }
}

