package com.openclassroom.chatopjava.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
@Data
@Entity
@Table(name="messages")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Model representing a message")

public class MessageModel {
    @Schema(description = "ID of the message", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
    @Schema(description = "ID of the associated rental", example = "1")
    @Column
    private Long rental_id;
    @Schema(description = "ID of the user who created the message", example = "1")
    @Column
    private Long user_id;
    @Schema(description = "Content of the message", example = "This is a message")
    @Column
    private String message;
    @Schema(description = "Date when the message was created", example = "2024-05-01T00:00:00.000Z")
    @Column
    private Date created_at;
    @Schema(description = "Date when the message was last updated", example = "2024-05-01T00:00:00.000Z")
    @Column
    private Date updated_at;
}
