package com.openclassroom.chatopjava.service;

import com.openclassroom.chatopjava.dto.UserCreateDto;
import com.openclassroom.chatopjava.dto.UserLoginDto;
import com.openclassroom.chatopjava.model.UserModel;
import com.openclassroom.chatopjava.repository.UserRepository;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Data
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    /**
     * Retrieves a user by their email.
     *
     * @param email the email of the user to retrieve
     * @return the user model
     */
    public UserModel getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    /**
     * Creates a new user.
     *
     * @param userDto the DTO containing the user details
     * @return the created user model
     */
    public UserModel createUser(UserCreateDto userDto) {
        UserModel user = modelMapper.map(userDto, UserModel.class);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setCreated_at(new Date());
        return userRepository.save(user);
    }
    /**
     * Checks if the provided password matches the stored password for the user.
     *
     * @param userLoginDto the DTO containing the login details
     * @param user         the user model
     * @return true if the passwords match, false otherwise
     */
    public boolean checkPassword(UserLoginDto userLoginDto, UserModel user) {
        return passwordEncoder.matches(userLoginDto.getPassword(), user.getPassword());
    }
}
