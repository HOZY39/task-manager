package com.simon.task_manager.solution;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Repository
public class SolutionRepository {

    private static final Logger log = LoggerFactory.getLogger(SolutionRepository.class);

    @Value("${file.upload-dir-img}")
    private String uploadDir;

    private final JdbcClient jdbcClient;

    public SolutionRepository(JdbcClient jdbcClient){
        this.jdbcClient = jdbcClient;
    }

    public List<Solution> findAll() {
        return jdbcClient.sql("""
            select s.*, COALESCE(STRING_AGG(si.image_url, ', '), '') AS images FROM solutions s 
            LEFT JOIN solution_images si ON s.id = si.solution_id
            GROUP BY s.id
            """)
                .query(Solution.class)
                .list();
    }

    public List<Solution> findAllForTask(Integer id) {
        return jdbcClient.sql("""
            SELECT s.*, COALESCE(STRING_AGG(si.image_url, ', '), '') AS images 
            FROM solutions s 
            LEFT JOIN solution_images si ON s.id = si.solution_id
            WHERE s.task_id = :id
            GROUP BY s.id;
            """)
                .param("id", id)
                .query(Solution.class)
                .list();
    }

    public Optional<Solution> findById(Integer id) {
        return jdbcClient.sql("""
            select s.*, COALESCE(STRING_AGG(si.image_url, ', '), '') AS images FROM solutions s 
            LEFT JOIN solution_images si ON s.id = si.solution_id 
            where id = :id
            GROUP BY s.id
            """)
                .param("id", id)
                .query(Solution.class)
                .optional();
    }
    public List<String> findImagesSol(Integer id) {
        return jdbcClient.sql("""
            select s.*, COALESCE(STRING_AGG(si.image_url, ', '), '') AS images AS images FROM solutions s 
            LEFT JOIN solution_images si ON s.id = si.solution_id 
            where solution_id = :id
            GROUP BY s.id
            """)
                .param("id", id)
                .query(String.class)
                .list();
    }

    public Integer create(Solution solution) {
        return jdbcClient.sql("""
                INSERT INTO solutions (task_id, solution, creator_username) 
                VALUES (?, ?, ?) RETURNING id
            """)
            .params(List.of(solution.task_id(), solution.solution(), solution.creator_username()))
            .query(Integer.class)
            .single();
    }

    public void addImage(MultipartFile file, Integer id) throws IOException {

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
        var updated = jdbcClient.sql("INSERT INTO solution_images(solution_id,image_url) values(?,?)")
                .params(List.of(id, filename))
                .update();
        Assert.state(updated==1, "Failed to add image");
    }

    public void update(Solution solution, Integer id){
        var updated = jdbcClient.sql("update solutions set solution = ? where id = ?")
                .params(List.of(solution.solution(), id))
                .update();
        Assert.state(updated==1, "Failed to update solution");
    }

    public void delete(Integer id){

        List<String> imageUrls = jdbcClient.sql("SELECT image_url FROM solution_images WHERE solution_id = :id")
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

        var deletedImg = jdbcClient.sql("delete from solution_images where solution_id = :id")
                .param("id", id)
                .update();

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
