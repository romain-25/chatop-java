package com.openclassroom.chatopjava.controller;

import com.openclassroom.chatopjava.dto.MessageDtoResponse;
import com.openclassroom.chatopjava.dto.RentalsListDto;
import com.openclassroom.chatopjava.model.RentalsModel;
import com.openclassroom.chatopjava.repository.RentalsRepository;
import com.openclassroom.chatopjava.service.JwtService;
import com.openclassroom.chatopjava.service.RentalsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class RentalsController {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private RentalsService rentalsService;
    @Autowired
    private RentalsRepository rentalsRepository;

    @Operation(summary = "Get all rentals", description = "Retrieve a list of all rentals")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = RentalsListDto.class)) }),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    @GetMapping("/rentals")
    public ResponseEntity<RentalsListDto> getAllRentals(@RequestHeader("Authorization") String token) {
        jwtService.validateToken(token);
        return new ResponseEntity<>(rentalsService.getAllRentals(), HttpStatus.OK);
    }
    @Operation(summary = "Get rental by ID", description = "Retrieve rental details by rental ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved rental",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = RentalsModel.class)) }),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Rental not found", content = @Content)
    })
    @GetMapping("/rentals/{id}")
    public ResponseEntity<RentalsModel> getRental(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        jwtService.validateToken(token);
        return new ResponseEntity<>(rentalsService.getRental(id), HttpStatus.OK);
    }
    @Operation(summary = "Create a new rental", description = "Create a new rental with the provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Rental created successfully",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = MessageDtoResponse.class)) }),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content)
    })
    @PostMapping("/rentals")
    public ResponseEntity<MessageDtoResponse> createRental(
            @RequestParam("name") String name,
            @RequestParam("surface") Long surface,
            @RequestParam("price") Long price,
            @RequestPart("picture") MultipartFile picture,
            @RequestParam("description") String description,
            @RequestHeader("Authorization") String token) throws IOException {
        jwtService.validateToken(token);
        return new ResponseEntity<>(rentalsService.createRental(name, surface, price, description, token, picture), HttpStatus.CREATED);
    }
    @Operation(summary = "Update a rental", description = "Update an existing rental with the provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rental updated successfully",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = MessageDtoResponse.class)) }),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Rental not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content)
    })
    @PutMapping("/rentals/{id}")
    public ResponseEntity<MessageDtoResponse> updateRental(
            @PathVariable("id") Long id,
            @RequestParam("name") String name,
            @RequestParam("surface") Long surface,
            @RequestParam("price") Long price,
            @RequestParam("description") String description,
            @RequestHeader("Authorization") String token) throws IOException {

        jwtService.validateToken(token);
        return new ResponseEntity<>(rentalsService.updateRental(id, name, surface, price, description, token), HttpStatus.OK);
    }
}
