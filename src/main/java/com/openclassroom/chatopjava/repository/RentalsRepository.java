package com.openclassroom.chatopjava.repository;

import com.openclassroom.chatopjava.model.RentalsModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
/**
 * Repository interface for Rentals entities.
 */
@Repository
public interface RentalsRepository extends CrudRepository<RentalsModel, Long> {
}
