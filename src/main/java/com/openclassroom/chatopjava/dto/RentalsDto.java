package com.openclassroom.chatopjava.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO for rental details")
public class RentalsDto {
    @Schema(description = "ID of the rental", example = "1")
    private Long id;
    @Schema(description = "Name of the rental", example = "Big Apartment")
    private String name;
    @Schema(description = "Price by night of the rental ", example = "100")
    private Long price;
    @Schema(description = "Surface area of the rental in square meters", example = "50")
    private Long surface;
    @Schema(description = "URL of the rental picture on AWS", example = "http://your_bucket.com/images/picture.jpg")
    private String picture;
    @Schema(description = "Description of the rental", example = "A cozy apartment in the city center")
    private String description;
    @Schema(description = "ID of the owner", example = "1")
    private Long owner_id;
    @Schema(description = "Creation date of the rental record", example = "2024-05-01T00:00:00.000Z")
    private Date created_at;
    @Schema(description = "Last update date of the rental record", example = "2024-05-01T00:00:00.000Z")
    private Date updated_at;
}
