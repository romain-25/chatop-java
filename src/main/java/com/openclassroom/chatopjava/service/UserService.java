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
    public Iterable<UserModel> getUsers() {
        return userRepository.findAll();
    }

    public UserModel getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public UserModel createUser(UserCreateDto userDto) {
        UserModel user = modelMapper.map(userDto, UserModel.class);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setCreatedAt(new Date());
        return userRepository.save(user);
    }

    public boolean checkPassword(UserLoginDto userLoginDto, UserModel user) {
        return passwordEncoder.matches(userLoginDto.getPassword(), user.getPassword());
    }
}
