package com.openclassroom.chatopjava.model;

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
public class MessageModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
    @Column
    private Long rental_id;
    @Column
    private Long user_id;
    @Column
    private String message;
    @Column
    private Date created_at;
    @Column
    private Date updated_at;
}
