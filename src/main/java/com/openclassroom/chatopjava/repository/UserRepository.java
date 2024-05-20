package com.openclassroom.chatopjava.repository;

import com.openclassroom.chatopjava.model.UserModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
/**
 * Repository interface for User entities.
 */
@Repository
public interface UserRepository extends CrudRepository<UserModel, Long> {
    /**
     * Finds a user by their email.
     *
     * @param email the email of the user to find
     * @return the user model
     */
    public UserModel findByEmail(String email);
}
