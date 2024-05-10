package com.openclassroom.chatopjava.controller;

import com.openclassroom.chatopjava.dto.RentalsListDto;
import com.openclassroom.chatopjava.model.RentalsModel;
import com.openclassroom.chatopjava.service.JwtService;
import com.openclassroom.chatopjava.service.RentalsService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class RentalsController {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private RentalsService rentalService;
    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/rentals")
    public ResponseEntity<RentalsListDto> getAllRentals(@RequestHeader("Authorization") String token) {
        jwtService.validateToken(token);
        return new ResponseEntity<>(rentalService.getAllRentals(), HttpStatus.OK);
    }

    @GetMapping("/rentals/{id}")
    public ResponseEntity<RentalsModel> getRental(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        jwtService.validateToken(token);
        return new ResponseEntity<>(rentalService.getRental(id), HttpStatus.OK);
    }

    @PostMapping("/rental/")
    public ResponseEntity<RentalsModel> createRental(@RequestBody RentalsModel rental, @RequestParam Long owner_id, @RequestParam String pathPicture, @RequestHeader("Authorization") String token) {
        jwtService.validateToken(token);
        return new ResponseEntity<>(rentalService.createRental(rental, owner_id, pathPicture), HttpStatus.CREATED);
    }

    @PutMapping("/rental/{id}")
    public ResponseEntity<RentalsModel> updateRental(@PathVariable Long id, @RequestParam Long ownerId, @RequestBody RentalsModel rental, @RequestHeader("Authorization") String token) {
        jwtService.validateToken(token);
        return new ResponseEntity<>(rentalService.updateRental(id, ownerId, rental), HttpStatus.OK);
    }
}
