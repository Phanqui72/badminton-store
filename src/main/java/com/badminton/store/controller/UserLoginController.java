package com.badminton.store.controller;

import com.badminton.store.dto.LoginRequest;
import com.badminton.store.feign.AuthFeignClient;
import com.badminton.store.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class UserLoginController {

    private final AuthFeignClient authFeignClient;

    public UserLoginController(AuthFeignClient authFeignClient) {
        this.authFeignClient = authFeignClient;
    }

    @PostMapping("/user-login")
    public ResponseEntity<?> loginDirectly(@RequestBody Map<String, String> body) {
        // 1. Lấy email từ JSON { "email": "phu@gmail.com" }
        String email = body.get("email");

        if (email == null || email.isEmpty()) {
            return ResponseEntity.badRequest().body("Email không được để trống!");
        }

        // 2. Chuẩn bị tham số Form-data để gửi sang Auth Server
        Map<String, String> params = new HashMap<>();
        params.put("grant_type", "email");
        params.put("email", email);

        try {
            // 3. Gọi trực tiếp Feign để lấy Token
            Map<String, Object> tokenResponse = authFeignClient.loginByEmail(params);
            return ResponseEntity.ok(tokenResponse);
        } catch (Exception e) {
            // Log lỗi 401 hoặc các lỗi kết nối khác
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Đăng nhập thất bại: " + e.getMessage());
        }
    }
}