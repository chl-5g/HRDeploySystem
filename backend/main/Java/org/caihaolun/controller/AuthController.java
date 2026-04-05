package org.caihaolun.controller;

import org.caihaolun.model.User;
import org.caihaolun.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepo;

    public AuthController(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        if (isBlank(req.email) || isBlank(req.password)) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "邮箱和密码不能为空"));
        }
        Optional<User> opt = userRepo.findByEmail(req.email.trim());
        if (opt.isEmpty() || !req.password.equals(opt.get().getPassword())) {
            return ResponseEntity.status(401).body(Map.of("success", false, "message", "用户名或密码错误"));
        }
        User u = opt.get();
        return ResponseEntity.ok(Map.of("success", true, "email", u.getEmail(), "username", u.getUsername()));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        if (isBlank(req.email) || isBlank(req.password) || isBlank(req.username)) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "所有字段必填"));
        }
        User user = new User();
        user.setEmail(req.email.trim());
        user.setUsername(req.username.trim());
        user.setPassword(req.password);
        userRepo.save(user);
        return ResponseEntity.ok(Map.of("success", true, "message", "注册成功"));
    }

    private boolean isBlank(String s) { return s == null || s.trim().isEmpty(); }

    public static class LoginRequest {
        public String email;
        public String password;
    }

    public static class RegisterRequest {
        public String email;
        public String username;
        public String password;
    }
}
