package com.openclassroom.chatopjava.service;

import com.openclassroom.chatopjava.dto.RentalsDto;
import com.openclassroom.chatopjava.dto.RentalsListDto;
import com.openclassroom.chatopjava.model.RentalsModel;
import com.openclassroom.chatopjava.model.UserModel;
import com.openclassroom.chatopjava.repository.RentalsRepository;
import com.openclassroom.chatopjava.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Service
public class RentalsService {
    @Autowired
    private RentalsRepository rentalsRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtService jwtService;

    public RentalsListDto getAllRentals() {
        Iterable<RentalsModel> rentalsModels = rentalsRepository.findAll();
        List<RentalsDto> rentalsDtos = new ArrayList<>();
        for (RentalsModel rentalsModel : rentalsModels) {
            RentalsDto rentalsDto = modelMapper.map(rentalsModel, RentalsDto.class);
            rentalsDtos.add(rentalsDto);
        }
        RentalsListDto rentalsListDto = new RentalsListDto();
        rentalsListDto.setRentals(rentalsDtos);
        return rentalsListDto;
    }

    public RentalsModel getRental(Long idRental) {
        return rentalsRepository.findById(idRental)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Location non trouvée"));
    }

    public RentalsModel createRental(String name, Long surface, Long price, String description, String bearerToken, MultipartFile picture ) throws IOException {

        String userEmail = jwtService.getSubjectFromToken(bearerToken);
        System.out.println(picture.getOriginalFilename());
        UserModel user = userRepository.findByEmail(userEmail);
        Path path = Paths.get("src/main/resources/public/images/" + picture.getOriginalFilename());
        String baseUrl = "http://localhost:8080/api/images/";
        byte[] bytes = picture.getBytes();
        Files.write(path, bytes);
        // save file to AWS S3
//        AmazonS3 s3client = AmazonS3ClientBuilder.defaultClient();
//        File convFile = new File(file.getOriginalFilename());
//        FileOutputStream fos = new FileOutputStream(convFile);
//        fos.write(file.getBytes());
//        fos.close();
//        s3client.putObject(new PutObjectRequest("your-bucket-name", file.getOriginalFilename(), convFile));
        RentalsModel rental = new RentalsModel();
        rental.setPicture(baseUrl + picture.getOriginalFilename());
        rental.setName(name);
        rental.setSurface(surface);
        rental.setPrice(price);
        rental.setDescription(description);
        rental.setOwner_id(user.getId());
        rental.setCreated_at(new Date());
        return rentalsRepository.save(rental);
    }

    public RentalsModel updateRental(Long rentalId, Long ownerId, RentalsModel newRental) {
        RentalsModel rentalToUpdate = rentalsRepository.findById(rentalId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Location non trouvée"));

        if (ownerId.equals(rentalToUpdate.getOwner_id())) {
            if (newRental.getName() != null) rentalToUpdate.setName(newRental.getName());
            if (newRental.getSurface() != null) rentalToUpdate.setSurface(newRental.getSurface());
            if (newRental.getPrice() != null) rentalToUpdate.setPrice(newRental.getPrice());
            if (newRental.getPicture() != null) rentalToUpdate.setPicture(newRental.getPicture());
            if (newRental.getDescription() != null) rentalToUpdate.setDescription(newRental.getDescription());
            rentalToUpdate.setUpdated_at(new Date());
            return rentalsRepository.save(rentalToUpdate);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Seul le propriétaire peut mettre à jour la location");
        }
    }
}
