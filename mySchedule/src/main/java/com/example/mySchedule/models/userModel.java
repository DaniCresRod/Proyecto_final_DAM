package com.example.mySchedule.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter                 //Lombok: Todos los getters
@Setter                 //Lombok: Todos los setters
@NoArgsConstructor      //Lombok: Constructor sin argumentos
@AllArgsConstructor     //Lombok: Constructor con todos los argumentos
@Table(name="Users")
public class userModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private long id;

    @Column(name = "Nif", unique = true)
    private String nif;

    @Column(name="Name")
    private String name;

    @Column(name="Alias")
    private String alias;

    @Column(name="Email")
    private String email;

    @Column(name="Password")
    private String password;

    @Column(name="Notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name="Price")
    private int price;

    @OneToMany(mappedBy = "userID",cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("user")
    private List<appointmentModel> appointmentsList;
}
