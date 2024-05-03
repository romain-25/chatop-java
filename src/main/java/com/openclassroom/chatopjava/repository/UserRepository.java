package com.openclassroom.chatopjava.repository;

import com.openclassroom.chatopjava.model.UserModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<UserModel, Long> {
    public UserModel findByEmail(String email);
}
