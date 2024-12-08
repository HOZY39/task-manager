package com.simon.task_manager.solution;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.simon.task_manager.task.Solution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import jakarta.annotation.PostConstruct;



@Repository
public class SolutionRepository {

    private static final Logger log = LoggerFactory.getLogger(SolutionRepository.class);

    private final JdbcClient jdbcClient;

    public SolutionRepository(JdbcClient jdbcClient){
        this.jdbcClient = jdbcClient;
    }

    public List<Solution> findAll() {
        return jdbcClient.sql("select * from solutions")
                .query(Solution.class)
                .list();
    }

    public Optional<Solution> findById(Integer id) {
        return jdbcClient.sql("SELECT * FROM solutions WHERE id = :id" )
                .param("id", id)
                .query(Solution.class)
                .optional();
    }

    public void create(Solution solution) {
        var updated = jdbcClient.sql("INSERT INTO solutions(task_id,solution) values(?,?)")
                .params(List.of(solution.task_id().toString(),solution.solution()))
                .update();

        Assert.state(updated == 1, "Failed to create solution");
    }

    public void update(Solution solution, Integer id){
        var updated = jdbcClient.sql("update solutions set task_id = ?, solution = ? where id = ?")
                .params(List.of(solution.task_id().toString(),solution.solution(), id))
                .update();
        Assert.state(updated==1, "Failed to update solution");
    }

    public void delete(Integer id){
        var deleted = jdbcClient.sql("delete from solutions where id = :id")
                .param("id", id)
                .update();
    }

    public int count() {
        return jdbcClient.sql("select * from solutions").query().listOfRows().size();
    }

    public void saveAll(List<Solution> solutions) {
        solutions.stream().forEach(this::create);
    }
}
