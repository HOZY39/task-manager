package com.simon.task_manager.auth;

import com.simon.task_manager.security.JwtUtil;
import com.simon.task_manager.user.User;
import com.simon.task_manager.user.UserController;
import com.simon.task_manager.user.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    public AuthController(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public void register(@RequestBody Map<String, String> request) {
        userRepository.registerUser(
                request.get("username"),
                request.get("email"),
                request.get("password"),
                request.get("role")
        );
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> request) {
        log.info("Logging in user: " + request.get("username"));
        User user = userRepository.findByUsername(request.get("username"))
                .orElseThrow(() -> new RuntimeException("User not found"));
        log.info("User found: " + user.username());
        if (!userRepository.checkPassword(user, request.get("password"))) {
            throw new RuntimeException("Invalid password");
        }
        log.info("Password correct");
        String token = jwtUtil.generateToken(user.username());
        log.info("Token generated: " + token);
        return Map.of("token", token);
    }
}
