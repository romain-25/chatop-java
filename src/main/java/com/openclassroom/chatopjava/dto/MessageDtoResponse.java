package com.openclassroom.chatopjava.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response DTO for creating a message")
public class MessageDtoResponse {
    @Schema(description = "The content of the message created", required = true, example = "This is a message")
    private String message;
}
