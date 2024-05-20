package com.openclassroom.chatopjava.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
@Data
@Entity
@Table(name = "rentals")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Model representing a rental")

public class RentalsModel {
    @Schema(description = "ID of the rental", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
    @Schema(description = "Name of the rental", example = "Big Apartment")
    @Column
    private String name;
    @Schema(description = "Surface area of the rental in square meters", example = "50")
    @Column
    private Long surface;
    @Schema(description = "Price by night of the rental ", example = "100")
    @Column
    private Long price;
    @Schema(description = "URL of the rental picture on AWS", example = "http://your_bucket.com/images/picture.jpg")
    @Column
    private String picture;
    @Schema(description = "Description of the rental", example = "A cozy apartment in the city center")
    @Column
    private String description;
    @Schema(description = "ID of the owner", example = "1")
    @Column
    private Long owner_id;
    @Schema(description = "Creation date of the rental record", example = "2024-05-01T00:00:00.000Z")
    @Column
    private Date created_at;
    @Schema(description = "Last update date of the rental record", example = "2024-05-01T00:00:00.000Z")
    @Column
    private Date updated_at;
}
