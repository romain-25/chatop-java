package com.openclassroom.chatopjava.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Data
@Entity
@Table(name = "users")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO for user details")

public class UserModel {
    @Schema(description = "ID of the user", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
    @Schema(description = "Name of the user", required = true, example = "Romain Rouabah")
    @Column
    private String name;
    @Schema(description = "Email address of the user", required = true, example = "romain.rouabah@example.com")
    @Column
    private String email;
    @Schema(description = "Password of the user", required = true, example = "password123")
    @Column
    private String password;
    @Schema(description = "Creation date of the user record", example = "2024-05-01T00:00:00.000Z")
    @Column
    private Date created_at;
    @Schema(description = "Last update date of the user record", example = "2024-05-01T00:00:00.000Z")
    @Column
    private Date updated_at;
}
