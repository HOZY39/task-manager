// package com.simon.task_manager.task;

// import org.springframework.jdbc.core.simple.JdbcClient;
// import org.springframework.stereotype.Repository;
// import org.springframework.util.Assert;

// import java.util.List;
// import java.util.Optional;

// @Repository
// public class JdbcTaskRepository implements TaskRepository {

//     private final JdbcClient jdbcClient;

//     public JdbcTaskRepository(JdbcClient jdbcClient) {
//         this.jdbcClient = jdbcClient;
//     }

//     public List<Task> findAll() {
//         return jdbcClient.sql("select * from Task")
//                 .query(Task.class)
//                 .list();
//     }

// }
