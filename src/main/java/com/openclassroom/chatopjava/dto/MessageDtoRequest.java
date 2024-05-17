package com.openclassroom.chatopjava.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MessageDtoRequest {
    @NotEmpty
    private String message;
    @NotNull
    private Long user_id;
    @NotNull
    private Long rental_id;
}
