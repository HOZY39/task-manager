package com.simon.task_manager.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.simon.task_manager.security.JwtUtil;
import com.simon.task_manager.task.TaskRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    public UserController(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.jwtUtil = jwtUtil;
    }

    @GetMapping
    List<User> findAll() {
        return userRepository.findAll();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    void register(@RequestBody User user) {
        userRepository.registerUser(user.username(), user.email(), user.password(), user.role());
    }

    @PostMapping("/login")
    String login(@RequestBody User user) {
        Optional<User> foundUser = userRepository.findByUsername(user.username());
        System.out.println("Wyszukiwanie zakończone. Znaleziono użytkownika: " + foundUser.isPresent());
        if (foundUser.isPresent() && passwordEncoder.matches(user.password(), foundUser.get().password())) {
            return jwtUtil.generateToken(user.username());
        } else {
            throw new UserNotFoundException();
        }
    }

    @GetMapping("/{username}/role")
    String getRole(@PathVariable String username) {
        return userRepository.findRoleByUsername(username)
                .orElseThrow(() -> new UserNotFoundException());
    }
}