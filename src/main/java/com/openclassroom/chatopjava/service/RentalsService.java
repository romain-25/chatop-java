package com.openclassroom.chatopjava.service;

import com.openclassroom.chatopjava.dto.RentalsDto;
import com.openclassroom.chatopjava.dto.RentalsListDto;
import com.openclassroom.chatopjava.model.RentalsModel;
import com.openclassroom.chatopjava.repository.RentalsRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class RentalsService {
    @Autowired
    private RentalsRepository rentalsRepository;
    @Autowired
    private ModelMapper modelMapper;
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

    public RentalsModel createRental(RentalsModel rental, Long owner_id, String pathPicture) {
        rental.setCreated_at(new Date());
        rental.setOwner_id(owner_id);
        rental.setPicture(pathPicture);
        return rentalsRepository.save(rental);
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
