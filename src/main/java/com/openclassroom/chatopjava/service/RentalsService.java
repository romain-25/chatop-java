package com.openclassroom.chatopjava.service;

import com.openclassroom.chatopjava.dto.RentalsDto;
import com.openclassroom.chatopjava.dto.RentalsListDto;
import com.openclassroom.chatopjava.model.RentalsModel;
import com.openclassroom.chatopjava.model.UserModel;
import com.openclassroom.chatopjava.repository.RentalsRepository;
import com.openclassroom.chatopjava.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

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
    @Value("${aws.accessKeyId}")
    private String awsAccessKeyId;

    @Value("${aws.secretAccessKey}")
    private String awsSecretAccessKey;

    @Value("${aws.s3.bucketName}")
    private String bucketName;

    private S3Client s3Client;
    @PostConstruct
    public void init() {
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(awsAccessKeyId, awsSecretAccessKey);
        this.s3Client = S3Client.builder()
                .region(Region.EU_NORTH_1)
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .build();
    }
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
        String fileUrl = uploadFileToS3(picture);
        RentalsModel rental = new RentalsModel();
//        rental.setPicture(baseUrl + picture.getOriginalFilename());
        rental.setPicture(fileUrl);
        rental.setName(name);
        rental.setSurface(surface);
        rental.setPrice(price);
        rental.setDescription(description);
        rental.setOwner_id(user.getId());
        rental.setCreated_at(new Date());
        return rentalsRepository.save(rental);
    }

    private String uploadFileToS3(MultipartFile file) throws IOException {
        String key = "images/" + file.getOriginalFilename();

        try {
            s3Client.putObject(PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(key)
                            .build(),
                    software.amazon.awssdk.core.sync.RequestBody.fromBytes(file.getBytes()));
        } catch (S3Exception e) {
            throw new RuntimeException("Failed to upload file to S3", e);
        }

        return s3Client.utilities().getUrl(builder -> builder.bucket(bucketName).key(key)).toExternalForm();
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
