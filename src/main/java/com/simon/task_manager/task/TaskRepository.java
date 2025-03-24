package com.simon.task_manager.task;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;


@Repository
public class TaskRepository {

    private static final Logger log = LoggerFactory.getLogger(TaskRepository.class);

    private final JdbcClient jdbcClient;

    public TaskRepository(JdbcClient jdbcClient){
        this.jdbcClient = jdbcClient;
    }

    public List<Task> findAll() {
        return jdbcClient.sql("select * from tasks")
            .query(Task.class)
            .list();
    }

    public Optional<Task> findById(Integer id) {
        return jdbcClient.sql("SELECT * FROM tasks WHERE id = :id" )
                .param("id", id)
                .query(Task.class)
                .optional();
    }
    public List<Task> findAllBySection(String section) {
        return jdbcClient.sql("SELECT * FROM tasks WHERE section = :section" )
                .param("section", section)
                .query(Task.class)
                .list();
    }

    public void create(Task task) {
        var updated = jdbcClient.sql("INSERT INTO tasks(subject,section,description,creator_username) values(?,?,?,?)")
                .params(List.of(task.subject().toString(),task.section(),task.description(),task.creator_username()))
                .update();

        Assert.state(updated == 1, "Failed to create task");
    }

    public void update(Task task, Integer id){
        var updated = jdbcClient.sql("update tasks set description = ?, where id = ?")
                .params(List.of(task.description(), id))
                .update();
        Assert.state(updated==1, "Failed to update task");
    }

    public void delete(Integer id){
        var deleted_sol = jdbcClient.sql("delete from solutions where task_id = :id")
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
