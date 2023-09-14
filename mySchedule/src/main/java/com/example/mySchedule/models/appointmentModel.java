package com.example.mySchedule.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="Appointments")
public class appointmentModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private long id;

    @NonNull
    @Column(name="Date")
    private LocalDate appoDate;

    @NonNull
    @Column(name="Start")
    private LocalTime appoStart;

    @Column(name="SessionNotes", columnDefinition = "TEXT")
    private String notes;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    @JsonBackReference  //evita el stockOverFlow
    private userModel userID;
}
