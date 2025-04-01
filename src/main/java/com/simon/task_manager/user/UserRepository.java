package com.simon.task_manager.user;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.simon.task_manager.task.TaskRepository;


@Repository
public class UserRepository {
    
    private static final Logger log = LoggerFactory.getLogger(UserRepository.class);

    private final JdbcClient jdbcClient;
    private final PasswordEncoder passwordEncoder;

    public UserRepository(JdbcClient jdbcClient){
        this.jdbcClient = jdbcClient;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public List<User> findAll() {
        return jdbcClient.sql("select * from users")
            .query(User.class)
            .list();
    }

    public void save(User user) {
        var updated = jdbcClient.sql("INSERT INTO users(username, email, password, role) values(?,?,?,?)")
                .params(List.of(user.username(), user.email(), user.password(), user.role()))
                .update();

        Assert.state(updated == 1, "Failed to create user");
    }
    public void registerUser(String username, String email, String password, String role) {
        if (findByUsername(username).isPresent()) {
            throw new RuntimeException("Username already taken");
        }
        User user = new User(username, email, passwordEncoder.encode(password), role);
        save(user);
    }

    public Optional<User> findByUsername(String username) {
        return jdbcClient.sql("SELECT * FROM users WHERE username = :username")
                .param("username", username)
                .query(User.class)
                .optional();
    }

    public Optional<String> findRoleByUsername(String username) {
        return jdbcClient.sql("SELECT role FROM users WHERE username = :username")
                .param("username", username)
                .query(String.class)
                .optional();
    }

    public boolean checkPassword(User user, String rawPassword) {
        return passwordEncoder.matches(rawPassword, user.password());
    }
}
