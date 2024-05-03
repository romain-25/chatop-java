package com.openclassroom.chatopjava.controller;

import com.openclassroom.chatopjava.service.JwtService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
    private JwtService jwtService;
    public LoginController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @PostMapping("/auth")
    public String getToken(Authentication authentication) {
        String token = jwtService.generateToken(authentication);
        return token;
    }

}
