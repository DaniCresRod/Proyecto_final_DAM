package com.example.mySchedule.repositories;

import com.example.mySchedule.models.userModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RepoUser extends JpaRepository<userModel, Long> {
    //En SQL nativo
    @Query(value="SELECT * FROM users WHERE email=:userEmail", nativeQuery = true)
    public userModel findByEmail(@Param("userEmail") String userEmail);

    //En JPA
    @Query("SELECT u FROM userModel u WHERE u.nif = :userNif")
    Optional<userModel> findByUserNif(@Param("userNif") String userNif);

}
