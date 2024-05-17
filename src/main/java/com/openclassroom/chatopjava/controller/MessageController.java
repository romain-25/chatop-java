package com.openclassroom.chatopjava.controller;

import com.openclassroom.chatopjava.dto.MessageDtoRequest;
import com.openclassroom.chatopjava.dto.MessageDtoResponse;
import com.openclassroom.chatopjava.service.JwtService;
import com.openclassroom.chatopjava.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class MessageController {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private MessageService messageService;

    @PostMapping("/messages")
    public ResponseEntity<MessageDtoResponse> createMessage(
            @RequestBody MessageDtoRequest message,
            @RequestHeader("Authorization") String token) throws IOException {
        jwtService.validateToken(token);
        return new ResponseEntity<>(messageService.creatMessage(message), HttpStatus.CREATED);
    }
}
