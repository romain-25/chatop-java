package com.openclassroom.chatopjava.model;

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
public class RentalsModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
    @Column
    private String name;
    @Column
    private Long surface;
    @Column
    private Long price;
    @Column
    private String picture;
    @Column
    private String description;
    @Column
    private Long owner_id;
    @Column
    private Date created_at;
    @Column
    private Date updated_at;
}
