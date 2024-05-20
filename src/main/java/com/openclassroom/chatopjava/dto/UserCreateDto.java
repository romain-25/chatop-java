package com.openclassroom.chatopjava.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Request DTO for user creation")

public class UserCreateDto {
    @Schema(description = "Name of the user", required = true, example = "Romain Rouabah")
    @NotBlank
    private String name;
    @Schema(description = "Email address of the user", required = true, example = "romain.rouabah@example.com")
    @NotBlank
    @Email
    private String email;
    @Schema(description = "Password of the user", required = true, example = "password123")
    @NotBlank
    private String password;
}
