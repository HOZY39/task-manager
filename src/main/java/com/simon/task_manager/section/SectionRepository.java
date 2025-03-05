package com.simon.task_manager.section;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import com.simon.task_manager.task.TaskRepository;


@Repository
public class SectionRepository {
    
    private static final Logger log = LoggerFactory.getLogger(TaskRepository.class);

    private final JdbcClient jdbcClient;

    public SectionRepository(JdbcClient jdbcClient){
        this.jdbcClient = jdbcClient;
    }

    public List<Section> findAll() {
        return jdbcClient.sql("select * from sections")
            .query(Section.class)
            .list();
    }
    public List<Section> findBySubject (String subject) {
        return jdbcClient.sql("select * from sections where subject = :subject")
        .param("subject", subject)
            .query(Section.class)
            .list();
    }
}
