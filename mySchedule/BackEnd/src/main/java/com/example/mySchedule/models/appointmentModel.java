package com.example.mySchedule.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonIgnore
    @Column(name="BillPath")
    private String billPath;

    @Column(name="generated_bill", columnDefinition = "BOOLEAN DEFAULT false")
    private boolean hasBill;

    //@JsonIgnore
    @Column(name="deleted", columnDefinition = "BOOLEAN DEFAULT false")
    private boolean deleted;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties("appointmentsList")
    private userModel userID;

    /**
     * Antes de realizar un guardado en la base de datos, revisa si hay un path
     * en el que se guarda la factura. Si es asi, establece el valor de hasBill en true
     */
    @PreUpdate
    public void prePersist() {
        // Si billPath no es nulo, establecer hasBill en true
        if (billPath != null) {
            hasBill = true;
        }
    }
}
