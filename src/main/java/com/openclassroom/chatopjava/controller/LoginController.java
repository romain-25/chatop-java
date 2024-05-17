package com.openclassroom.chatopjava.controller;

import com.openclassroom.chatopjava.dto.TokenDto;
import com.openclassroom.chatopjava.dto.UserCreateDto;
import com.openclassroom.chatopjava.dto.UserLoginDto;
import com.openclassroom.chatopjava.model.UserModel;
import com.openclassroom.chatopjava.service.JwtService;
import com.openclassroom.chatopjava.service.UserService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
//@CrossOrigin(origins = "http://localhost:4200")
public class LoginController {
    private JwtService jwtService;
    private UserService userService;
    @Autowired
    private ModelMapper modelMapper;
    public LoginController(JwtService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserLoginDto user) {
        System.out.println(user.toString());
        UserModel existingUser = userService.getUserByEmail(user.getEmail());
        if (existingUser != null && userService.checkPassword(user, existingUser)) {
            Authentication authentication = new UsernamePasswordAuthenticationToken(existingUser.getEmail(), null, new ArrayList<>());
            String token = jwtService.generateToken(authentication);
            TokenDto tokenDto = new TokenDto(token);
            return ResponseEntity.ok(tokenDto);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email ou mot de passe incorrect");
        }
    }

    @PostMapping("/auth/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserCreateDto user) {
        System.out.println(user.toString());
        UserModel existingUser = userService.getUserByEmail(user.getEmail());
        if (existingUser == null) {
            UserModel newUser = userService.createUser(user);
            Authentication authentication = new UsernamePasswordAuthenticationToken(newUser.getEmail(), null, new ArrayList<>());
            String token = jwtService.generateToken(authentication);
            TokenDto tokenDto = new TokenDto(token);
            return ResponseEntity.ok(tokenDto);
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Un utilisateur avec cet email existe déjà");
        }
    }

    @GetMapping("/auth/me")
    public ResponseEntity<?> me(Authentication authentication) {
        UserModel currentUser = userService.getUserByEmail(authentication.getName());
        return ResponseEntity.ok(currentUser);
    }

}