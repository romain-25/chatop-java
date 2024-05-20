package com.openclassroom.chatopjava.repository;

import com.openclassroom.chatopjava.model.MessageModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
/**
 * Repository interface for Message entities.
 */
@Repository
public interface MessageRepository extends CrudRepository<MessageModel, Long> {
}
