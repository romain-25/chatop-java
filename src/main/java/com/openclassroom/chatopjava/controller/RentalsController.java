package com.openclassroom.chatopjava.controller;

import com.openclassroom.chatopjava.dto.RentalsListDto;
import com.openclassroom.chatopjava.model.RentalsModel;
import com.openclassroom.chatopjava.repository.RentalsRepository;
import com.openclassroom.chatopjava.service.JwtService;
import com.openclassroom.chatopjava.service.RentalsService;
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
@CrossOrigin(origins = "http://localhost:4200")
public class RentalsController {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private RentalsService rentalsService;
    @Autowired
    private RentalsRepository rentalsRepository;
//    @Autowired
//    private ModelMapper modelMapper;

    @GetMapping("/rentals")
    public ResponseEntity<RentalsListDto> getAllRentals(@RequestHeader("Authorization") String token) {
        jwtService.validateToken(token);
        return new ResponseEntity<>(rentalsService.getAllRentals(), HttpStatus.OK);
    }

    @GetMapping("/rentals/{id}")
    public ResponseEntity<RentalsModel> getRental(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        jwtService.validateToken(token);
        return new ResponseEntity<>(rentalsService.getRental(id), HttpStatus.OK);
    }

    @PostMapping("/rentals")
    public ResponseEntity<RentalsModel> createRental(@RequestParam("name") String name,
                                                     @RequestParam("surface") Long surface,
                                                     @RequestParam("price") Long price,
                                                     @RequestPart("picture") MultipartFile picture,
                                                     @RequestParam("description") String description,
                                                     @RequestHeader("Authorization") String token) throws IOException {
        jwtService.validateToken(token);
        return new ResponseEntity<>(rentalsService.createRental(name, surface, price, description, token, picture), HttpStatus.CREATED);
    }

    @PutMapping("/rental/{id}")
    public ResponseEntity<RentalsModel> updateRental(@PathVariable Long id, @RequestParam Long ownerId, @RequestBody RentalsModel rental, @RequestHeader("Authorization") String token) {
        jwtService.validateToken(token);
        return new ResponseEntity<>(rentalsService.updateRental(id, ownerId, rental), HttpStatus.OK);
    }
}
