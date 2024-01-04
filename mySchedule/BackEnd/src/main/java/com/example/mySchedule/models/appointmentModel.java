package com.example.mySchedule.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

    @Column(name="BillPath")
    private String billPath;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties("appointmentsList")
    private userModel userID;

    public enum AppoType
    {
        Terapia,
        Formacion,
        Grupos,
        Otros
    }
}
