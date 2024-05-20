package com.openclassroom.chatopjava.controller;

import com.openclassroom.chatopjava.dto.MessageDtoRequest;
import com.openclassroom.chatopjava.dto.MessageDtoResponse;
import com.openclassroom.chatopjava.service.JwtService;
import com.openclassroom.chatopjava.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(summary = "Create a new message", description = "Create a new message and return the created message for creation confirmation notification")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Message created successfully",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = MessageDtoResponse.class)) }),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content)
    })
    @PostMapping("/messages")
    public ResponseEntity<MessageDtoResponse> createMessage(
            @RequestBody MessageDtoRequest message,
            @RequestHeader("Authorization") String token) throws IOException {
        jwtService.validateToken(token);
        return new ResponseEntity<>(messageService.creatMessage(message), HttpStatus.CREATED);
    }
}
