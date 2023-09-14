package com.example.mySchedule.repositories;

import com.example.mySchedule.models.appointmentModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;

public interface RepoAppointment extends JpaRepository<appointmentModel, Long> {

    @Query(value = "SELECT EXISTS(SELECT * FROM appointments WHERE   ", nativeQuery = true )
    int findRepeatedAppo(@Param("myAppo")appointmentModel targetAppo);

}
