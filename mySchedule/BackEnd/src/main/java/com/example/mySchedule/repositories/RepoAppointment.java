package com.example.mySchedule.repositories;

import com.example.mySchedule.models.appointmentModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public interface RepoAppointment extends JpaRepository<appointmentModel, Long> {
    @Query(value = "SELECT * FROM appointments WHERE date=:targetDate", nativeQuery = true )
    List<appointmentModel> findAppoByDate(@Param("targetDate") LocalDate targetDate);

    @Query(value = "SELECT * FROM appointments WHERE date=:#{#myAppo.getAppoDate()} " +
            "AND start BETWEEN DATE_ADD(STR_TO_DATE(:#{#myAppo.getAppoStart()}, '%H:%i:%s'), INTERVAL -75 MINUTE) " +
            "AND DATE_ADD(STR_TO_DATE(:#{#myAppo.getAppoStart()}, '%H:%i:%s'), INTERVAL 75 MINUTE)", nativeQuery = true)
    List<appointmentModel> findRepeatedAppoHour(@Param("myAppo")appointmentModel targetAppo);

    @Query(value = "EXIST(SELECT * FROM appointments WHERE id!=:#{#myAppo.getId()} " +
            "date=:#{#myAppo.getAppoDate()} " +
            "AND start BETWEEN DATE_ADD(STR_TO_DATE(:#{#myAppo.getAppoStart()}, '%H:%i:%s'), INTERVAL -75 MINUTE) " +
            "AND DATE_ADD(STR_TO_DATE(:#{#myAppo.getAppoStart()}, '%H:%i:%s'), INTERVAL 75 MINUTE))", nativeQuery = true)
    boolean findRepeatedAppoHourWithUserID(@Param("myAppo")appointmentModel targetAppo);

    @Query(value = "SELECT * FROM appointments WHERE date BETWEEN :startDate AND :endDate", nativeQuery = true )
    List<appointmentModel> findAppoBetweenDates(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
