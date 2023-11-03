package com.example.mySchedule.repositories;

import com.example.mySchedule.models.userModel;
import org.hibernate.annotations.Parameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RepoUser extends JpaRepository<userModel, Long> {
    @Query(value="SELECT * FROM users WHERE email=:userEmail", nativeQuery = true)
    public userModel findByEmail(@Param("userEmail") String userEmail);

    @Query(value="SELECT * FROM users WHERE nif=:userNif", nativeQuery = true)
    public userModel findByNif(@Param("userNif") String userNif);

    @Query("SELECT u FROM userModel u WHERE u.name = :userName")
    Optional<userModel> findByUsername(@Param("userName") String userName);
}
