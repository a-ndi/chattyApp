package com.work.chatty.controller;

import com.work.chatty.dto.LoginRequest;
import com.work.chatty.dto.RegisterRequest;
import com.work.chatty.dto.UserResponse;
import com.work.chatty.model.User;
import com.work.chatty.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/auth")
public class AuthController {

        @Autowired
        AuthService authService;

        @PostMapping("/register")
        public String register(@RequestBody RegisterRequest req) {
            return authService.register(req);
        }

        @PostMapping("/login")
        public UserResponse login(@RequestBody LoginRequest req) {
            User user = authService.login(req);
            return toUserResponse(user);
        }

        private UserResponse toUserResponse(User user) {
            return new UserResponse(user.getId(), user.getUsername());
        }

}
