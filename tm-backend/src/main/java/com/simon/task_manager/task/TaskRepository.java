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
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import com.simon.task_manager.solution.SolutionRepository;
import com.simon.task_manager.solution.Solution;


@Repository
public class TaskRepository {

    @Value("${file.upload-dir-img}")
    private String uploadDir;

    private final SolutionRepository solutionRepository;

    private static final Logger log = LoggerFactory.getLogger(TaskRepository.class);

    private final JdbcClient jdbcClient;

    public TaskRepository(JdbcClient jdbcClient, SolutionRepository solutionRepository){
        this.jdbcClient = jdbcClient;
        this.solutionRepository = solutionRepository;
    }

    public List<Task> findAll() {
        return jdbcClient.sql("""
            SELECT t.*, COALESCE(STRING_AGG(qi.image_url, ', '), '') AS images FROM tasks t 
            LEFT JOIN question_images qi ON t.id = qi.question_id
            GROUP BY t.id
            """)
            .query(Task.class)
            .list();
    }

    public Optional<Task> findById(Integer id) {
        return jdbcClient.sql("""
            SELECT t.*, COALESCE(STRING_AGG(qi.image_url, ', '), '') AS images 
            FROM tasks t 
            LEFT JOIN question_images qi ON t.id = qi.question_id 
            WHERE t.id = :id
            GROUP BY t.id
            """)
                .param("id", id)
                .query(Task.class)
                .optional();
    }
    public List<Task> findAllBySection(String section) {
        return jdbcClient.sql("""
            SELECT t.*, COALESCE(STRING_AGG(qi.image_url, ', '), '') AS images FROM tasks t 
            LEFT JOIN question_images qi ON t.id = qi.question_id 
            WHERE t.section = :section
            GROUP BY t.id
            """)
                .param("section", section)
                .query(Task.class)
                .list();
    }
    public List<Task> findAllByText(String description) {
        return jdbcClient.sql("""
            SELECT t.*, COALESCE(STRING_AGG(qi.image_url, ', '), '') AS images FROM tasks t 
            LEFT JOIN question_images qi ON t.id = qi.question_id 
            WHERE description LIKE :description
            GROUP BY t.id
            """ )
                .param("description", "%" + description + "%")
                .query(Task.class)
                .list();
    }

    public Integer create(Task task) {
        return jdbcClient.sql("INSERT INTO tasks(subject,section,description,creator_username) values(?,?,?,?) RETURNING id")
                .params(List.of(task.subject().toString(),task.section(),task.description(),task.creator_username()))
                .query(Integer.class)
                .single();
    }

    public void addImage(MultipartFile file, Integer id) throws IOException {
        log.info("Uploading image for task with id: " + id);
        log.info("File name: " + file.getOriginalFilename());
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            boolean created = dir.mkdirs();
            if (!created) {
                throw new RuntimeException("Failed to create directory: " + uploadDir);
            }
        }

        String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir, filename);

        Files.write(filePath, file.getBytes());
        var updated = jdbcClient.sql("INSERT INTO question_images(question_id,image_url) values(?,?)")
                .params(List.of(id, filename))
                .update();
        Assert.state(updated==1, "Failed to add image");
    }

    public void update(Task task, Integer id){
        var updated = jdbcClient.sql("update tasks set description = ?, where id = ?")
                .params(List.of(task.description(), id))
                .update();
        Assert.state(updated==1, "Failed to update task");
    }

    public void delete(Integer id){

        List<String> imageUrls = jdbcClient.sql("SELECT image_url FROM question_images WHERE question_id = :id")
            .param("id", id)
            .query(String.class)
            .list();

        for (String imageUrl : imageUrls) {
            try {
                Path filePath = Paths.get("uploads/images", imageUrl);
                Files.deleteIfExists(filePath);
            } catch (Exception e) {
                System.err.println("Error while deleting file: " + imageUrl);
                e.printStackTrace();
            }
        }
        List<Solution> solutions = solutionRepository.findAllForTask(id);
        for (Solution solution : solutions) {
            solutionRepository.delete(solution.id());
        }
        
        var deletedImg = jdbcClient.sql("delete from question_images where question_id = :id")
                .param("id", id)
                .update();
        var deleted = jdbcClient.sql("delete from tasks where id = :id")
                .param("id", id)
                .update();
        Assert.state(deleted==1, "Failed to delete task");
    }

    public int count() {
        return jdbcClient.sql("select * from tasks").query().listOfRows().size();
    }

    public void saveAll(List<Task> tasks) {
        tasks.stream().forEach(this::create);
    }
}
