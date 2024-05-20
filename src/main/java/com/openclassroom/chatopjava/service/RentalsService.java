package com.openclassroom.chatopjava.service;

import com.openclassroom.chatopjava.dto.MessageDtoResponse;
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
    /**
     * Retrieves all rentals.
     *
     * @return a DTO containing the list of all rentals
     */
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
    /**
     * Retrieves a rental by its ID.
     *
     * @param idRental the ID of the rental to retrieve
     * @return the rental model
     * @throws ResponseStatusException if the rental is not found
     */
    public RentalsModel getRental(Long idRental) {
        return rentalsRepository.findById(idRental)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Location not found"));
    }
    /**
     * Creates a new rental.
     *
     * @param name         the name of the rental
     * @param surface      the surface area of the rental
     * @param price        the price of the rental
     * @param description  the description of the rental
     * @param bearerToken  the authentication token of the user
     * @param picture      the picture file of the rental
     * @return a response DTO with a success message
     * @throws IOException if there is an error processing the picture file
     */
    public MessageDtoResponse createRental(String name, Long surface, Long price, String description, String bearerToken, MultipartFile picture ) throws IOException {
        String userEmail = jwtService.getSubjectFromToken(bearerToken);
        UserModel user = userRepository.findByEmail(userEmail);
        RentalsModel rental = new RentalsModel();
        String fileUrl = uploadFileToS3(picture);
        rental.setPicture(fileUrl);
        rental.setName(name);
        rental.setSurface(surface);
        rental.setPrice(price);
        rental.setDescription(description);
        rental.setOwner_id(user.getId());
        rental.setCreated_at(new Date());
        MessageDtoResponse messageDtoResponse = new MessageDtoResponse();
        messageDtoResponse.setMessage("Your real estate ad has been created");
        rentalsRepository.save(rental);
        return messageDtoResponse;
    }
    /**
     * Uploads a file to S3.
     *
     * @param file the file to upload
     * @return the URL of the uploaded file
     * @throws IOException if there is an error processing the file
     * @throws RuntimeException if there is an error uploading the file to S3
     */
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
    /**
     * Updates an existing rental.
     *
     * @param rentalId    the ID of the rental to update
     * @param name        the new name of the rental
     * @param surface     the new surface area of the rental
     * @param price       the new price of the rental
     * @param description the new description of the rental
     * @param bearerToken the authentication token of the user
     * @return a response DTO with a success message
     * @throws ResponseStatusException if the rental is not found or the user is not authorized
     */
    public MessageDtoResponse updateRental(Long rentalId, String name, Long surface, Long price, String description, String bearerToken) {
        RentalsModel rentalToUpdate = rentalsRepository.findById(rentalId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Location not found"));
        String userEmail = jwtService.getSubjectFromToken(bearerToken);
        UserModel user = userRepository.findByEmail(userEmail);
        if (user.getId().equals(rentalToUpdate.getOwner_id())) {
            if (name!= null) rentalToUpdate.setName(name);
            if (surface != null) rentalToUpdate.setSurface(surface);
            if (price != null) rentalToUpdate.setPrice(price);
            if (description != null) rentalToUpdate.setDescription(description);
            rentalToUpdate.setUpdated_at(new Date());
            rentalsRepository.save(rentalToUpdate);
            MessageDtoResponse messageDtoResponse = new MessageDtoResponse();
            messageDtoResponse.setMessage("Your real estate ad has been updated");
            return messageDtoResponse;
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Only the owner can update the listing'");
        }
    }
}
