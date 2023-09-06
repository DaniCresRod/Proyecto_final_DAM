package com.example.mySchedule.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter                 //Lombok: Todos los getters
@Setter                 //Lombok: Todos los setters
@NoArgsConstructor      //Lombok: Constructor sin argumentos
@AllArgsConstructor     //Lombok: Constructor con todos los argumentos
@Table(name="timeTables")
public class userModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private int id;

    @Column(name="Name")
    private String name;

    @NonNull
    @Column(name="Date")
    private LocalDate appoDate;

    @NonNull
    @Column(name="Start")
    private LocalTime appoStart;

    @Column(name="Notes")
    private String notes;
}
