package com.openclassroom.chatopjava.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Response DTO for rental operations")

public class RentalsResponseDto {
    @Schema(description = "Response message", example = "Rental created successfully")
    private String message;
}
