package com.openclassroom.chatopjava.controller;

import com.openclassroom.chatopjava.model.UserModel;
import com.openclassroom.chatopjava.service.JwtService;
import com.openclassroom.chatopjava.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class LoginController {
    private JwtService jwtService;
    private UserService userService;
    public LoginController(JwtService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody UserModel user) {
        System.out.println(user.toString());
        UserModel existingUser = userService.getUserByEmail(user.getEmail());
        if (existingUser != null && existingUser.getPassword().equals(user.getPassword())) {
            Authentication authentication = new UsernamePasswordAuthenticationToken(existingUser.getEmail(), null, new ArrayList<>());
            String token = jwtService.generateToken(authentication);
            return ResponseEntity.ok(token);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email ou mot de passe incorrect");
        }
    }

    @PostMapping("/auth/register")
    public ResponseEntity<?> register(@RequestBody UserModel user) {
        System.out.println(user.toString());
        UserModel existingUser = userService.getUserByEmail(user.getEmail());
        if (existingUser == null) {
            UserModel newUser = userService.createUser(user);
            Authentication authentication = new UsernamePasswordAuthenticationToken(newUser.getEmail(), null, new ArrayList<>());
            String token = jwtService.generateToken(authentication);
            return ResponseEntity.ok(token);
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Un utilisateur avec cet email existe déjà");
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication authentication) {
        UserModel currentUser = userService.getUserByEmail(authentication.getName());
        return ResponseEntity.ok(currentUser);
    }

}

//curl -X POST -d '{"email":"test@test.fr"},"password":"test-25"' -H "Content-Type: application/json" http://localhost:8080/auth/login