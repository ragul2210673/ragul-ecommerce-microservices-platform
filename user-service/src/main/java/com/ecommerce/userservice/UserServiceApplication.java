package com.ecommerce.userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.*;
import java.time.LocalDateTime;

@SpringBootApplication
@RestController
@CrossOrigin(origins = "*")
public class UserServiceApplication {

    private final List<User> users = new ArrayList<>();
    private Long nextId = 1L;

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }

    // Initialize sample data
    public UserServiceApplication() {
        initializeUsers();
    }

    private void initializeUsers() {
        users.add(new User(nextId++, "John Doe", "john@example.com", "password123", "customer"));
        users.add(new User(nextId++, "Jane Smith", "jane@example.com", "password123", "admin"));
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "healthy");
        response.put("service", "user-service");
        response.put("timestamp", LocalDateTime.now());
        response.put("usersCount", users.size());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/users")
    public ResponseEntity<Map<String, Object>> getAllUsers() {
        List<UserResponse> userResponses = users.stream()
            .map(this::toUserResponse)
            .toList();
            
        Map<String, Object> response = new HashMap<>();
        response.put("users", userResponses);
        response.put("count", users.size());
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/users/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody RegisterRequest request) {
        if (request.getName() == null || request.getEmail() == null || request.getPassword() == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Missing required fields: name, email, password");
            return ResponseEntity.badRequest().body(response);
        }

        boolean userExists = users.stream()
            .anyMatch(u -> u.getEmail().equalsIgnoreCase(request.getEmail()));
            
        if (userExists) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "User with this email already exists");
            return ResponseEntity.badRequest().body(response);
        }

        User newUser = new User(
            nextId++,
            request.getName(),
            request.getEmail().toLowerCase(),
            request.getPassword(), // In production, hash this password
            "customer"
        );
        
        users.add(newUser);
        
        String token = generateToken(newUser);
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "User registered successfully");
        response.put("user", toUserResponse(newUser));
        response.put("token", token);
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/users/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest request) {
        if (request.getEmail() == null || request.getPassword() == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Email and password are required");
            return ResponseEntity.badRequest().body(response);
        }

        Optional<User> userOpt = users.stream()
            .filter(u -> u.getEmail().equalsIgnoreCase(request.getEmail()))
            .findFirst();
            
        if (userOpt.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Invalid credentials");
            return ResponseEntity.status(401).body(response);
        }
        
        User user = userOpt.get();
        
        if (!user.getPassword().equals(request.getPassword())) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Invalid credentials");
            return ResponseEntity.status(401).body(response);
        }
        
        String token = generateToken(user);
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Login successful");
        response.put("user", toUserResponse(user));
        response.put("token", token);
        
        return ResponseEntity.ok(response);
    }

    private UserResponse toUserResponse(User user) {
        return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getRole(), user.getCreatedAt());
    }

    private String generateToken(User user) {
        return "token_" + user.getId() + "_" + System.currentTimeMillis();
    }
}

// User Entity
class User {
    private Long id;
    private String name;
    private String email;
    private String password;
    private String role;
    private LocalDateTime createdAt;

    public User(Long id, String name, String email, String password, String role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}

// DTOs
class UserResponse {
    private Long id;
    private String name;
    private String email;
    private String role;
    private LocalDateTime createdAt;

    public UserResponse(Long id, String name, String email, String role, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.createdAt = createdAt;
    }

    // Getters
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}

class RegisterRequest {
    private String name;
    private String email;
    private String password;

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}

class LoginRequest {
    private String email;
    private String password;

    // Getters and Setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}