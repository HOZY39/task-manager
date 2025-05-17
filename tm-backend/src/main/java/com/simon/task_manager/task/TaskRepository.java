package com.simon.task_manager.task;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
public class TaskRepository {

    private static final Logger log = LoggerFactory.getLogger(TaskRepository.class);

    private final JdbcClient jdbcClient;

    public TaskRepository(JdbcClient jdbcClient){
        this.jdbcClient = jdbcClient;
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

    public int updateDescription(Integer taskId, String description) {
        return jdbcClient.sql("""
            UPDATE tasks SET description = ? WHERE id = ?
        """).params(List.of(description, taskId)).update();
    }

    public int insertImage(Integer taskId, String filename) {
        return jdbcClient.sql("""
            INSERT INTO question_images(question_id, image_url) VALUES (?, ?)
        """).params(List.of(taskId, filename)).update();
    }

    public int deleteImagesByTaskId(Integer taskId) {
        return jdbcClient.sql("DELETE FROM question_images WHERE question_id = :id")
                .param("id", taskId)
                .update();
    }

    public int deleteTaskById(Integer taskId) {
        return jdbcClient.sql("DELETE FROM tasks WHERE id = :id")
                .param("id", taskId)
                .update();
    }

    public List<String> findImageUrls(Integer taskId) {
        return jdbcClient.sql("SELECT image_url FROM question_images WHERE question_id = :id")
                .param("id", taskId)
                .query(String.class)
                .list();
    }

    public int count() {
        return jdbcClient.sql("SELECT COUNT(*) FROM tasks").query(Integer.class).single();
    }

    public void saveAll(List<Task> tasks) {
        tasks.stream().forEach(this::create);
    }
}
