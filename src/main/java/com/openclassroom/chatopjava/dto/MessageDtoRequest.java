package com.openclassroom.chatopjava.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request DTO for creating a message")
public class MessageDtoRequest {
    @Schema(description = "The content of the message", required = true, example = "This is a message")
    @NotEmpty
    private String message;
    @Schema(description = "ID of the user creating the message", required = true, example = "1")
    @NotNull
    private Long user_id;
    @Schema(description = "ID of the rental associated with the message", required = true, example = "1")
    @NotNull
    private Long rental_id;
}
