package com.openclassroom.chatopjava.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO for a list of rentals")
public class RentalsListDto {
    @Schema(description = "List of rental DTOs")
    List<RentalsDto> rentals;
}
