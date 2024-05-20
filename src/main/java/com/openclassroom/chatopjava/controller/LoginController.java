package com.openclassroom.chatopjava.controller;

import com.openclassroom.chatopjava.dto.TokenDto;
import com.openclassroom.chatopjava.dto.UserCreateDto;
import com.openclassroom.chatopjava.dto.UserLoginDto;
import com.openclassroom.chatopjava.model.UserModel;
import com.openclassroom.chatopjava.service.JwtService;
import com.openclassroom.chatopjava.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
public class LoginController {
    private JwtService jwtService;
    private UserService userService;
    @Autowired
    private ModelMapper modelMapper;
    public LoginController(JwtService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }
    @Operation(summary = "User login", description = "Authenticate user and return a JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful login",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = TokenDto.class)) }),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content)
    })
    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserLoginDto user) {
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
    @Operation(summary = "User registration", description = "Register a new user and return a JWT token for login with new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful registration",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = TokenDto.class)) }),
            @ApiResponse(responseCode = "409", description = "Conflict - User already exists",
                    content = @Content)
    })
    @PostMapping("/auth/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserCreateDto user) {
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
    @Operation(summary = "Get current user", description = "Get the authenticated user's details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = UserModel.class)) })
    })
    @GetMapping("/auth/me")
    public ResponseEntity<?> me(Authentication authentication) {
        UserModel currentUser = userService.getUserByEmail(authentication.getName());
        return ResponseEntity.ok(currentUser);
    }

}