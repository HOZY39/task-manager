package com.simon.task_manager.subject;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;


@Repository
public class SubjectRepository {
    
    private static final Logger log = LoggerFactory.getLogger(SubjectRepository.class);

    private final JdbcClient jdbcClient;

    public SubjectRepository(JdbcClient jdbcClient){
        this.jdbcClient = jdbcClient;
    }

    public List<Subject> findAll() {
        return jdbcClient.sql("select * from subjects")
            .query(Subject.class)
            .list();
    }
}
