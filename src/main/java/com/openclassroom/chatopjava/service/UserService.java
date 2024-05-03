package com.openclassroom.chatopjava.service;

import com.openclassroom.chatopjava.model.UserModel;
import com.openclassroom.chatopjava.repository.UserRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Data
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    public Iterable<UserModel> getUsers() {
        return userRepository.findAll();
    }
    public UserModel getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    public UserModel createUser(UserModel user) {
        user.setCreatedAt(new Date());
        return userRepository.save(user);
    }
}
