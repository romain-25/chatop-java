package com.openclassroom.chatopjava.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO for user details")

public class UserDTO {
    @Schema(description = "ID of the user", example = "1")
    private Long id;
    @Schema(description = "Name of the user", required = true, example = "Romain Rouabah")
    private String name;
    @Schema(description = "Email address of the user", required = true, example = "romain.rouabah@example.com")
    private String email;
    @Schema(description = "Password of the user", required = true, example = "password123")
    private String password;
    @Schema(description = "Creation date of the user record", example = "2024-05-01T00:00:00.000Z")
    private Date createdAt;
    @Schema(description = "Last update date of the user record", example = "2024-05-01T00:00:00.000Z")
    private Date updatedAt;
}
