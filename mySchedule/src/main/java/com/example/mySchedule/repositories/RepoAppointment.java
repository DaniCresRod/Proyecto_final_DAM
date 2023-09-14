package com.example.mySchedule.repositories;

import com.example.mySchedule.models.appointmentModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public interface RepoAppointment extends JpaRepository<appointmentModel, Long> {
    @Query(value = "SELECT * FROM appointments WHERE date=:targetDate", nativeQuery = true )
    List<appointmentModel> findRepeatedAppoDate(@Param("targetDate") LocalDate targetDate);

    @Query(value = "SELECT * FROM appointments WHERE start BETWEEN :#{#myAppo.getAppoStart()} AND DATE_ADD(:#{#myAppo.getAppoStart()}, INTERVAL 1 HOUR 15 MINUTE) LIMIT 1", nativeQuery = true)
    appointmentModel findRepeatedAppoHour(@Param("myAppo")appointmentModel targetAppo);
}
