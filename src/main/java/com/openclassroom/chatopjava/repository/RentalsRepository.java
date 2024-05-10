package com.openclassroom.chatopjava.repository;

import com.openclassroom.chatopjava.model.RentalsModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RentalsRepository extends JpaRepository<RentalsModel, Long> {
}
