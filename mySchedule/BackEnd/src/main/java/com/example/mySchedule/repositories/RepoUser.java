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

    //@Query(value = "SELECT * FROM users u, appointments a WHERE u.id=:userIdBuscado AND u.id=a.user_id AND a.deleted='false'", nativeQuery = true)
//    @Query("SELECT u FROM userModel u LEFT JOIN u.appointments a WHERE u.id = :userIdBuscado AND a.deleted = false")
//    Optional<userModel> findByIdWDeleted(@Param("userIdBuscado") long id);

//    @Query("SELECT u FROM userModel u WHERE u.id = :userIdBuscado")
//    Optional<userModel> findByIdWDeleted(@Param("userIdBuscado") long id);

//    @Query("SELECT u FROM userModel u LEFT JOIN u.appointments a WHERE u.id = :userIdBuscado AND a.deleted = false")
//    @Query(value = "SELECT u.id, u.alias, u.email, u.Nombre, u.nif, u.notes, u.price, u.apellido1, u.rol, u.apellido2, u.telefono, u.deleted, u.password "
//            + "FROM users u, appointments a WHERE u.id = a.user_id AND u.id = :userIdBuscado AND u.deleted = true", nativeQuery = true)
//    Optional<userModel> findByIdWDeleted(@Param("userIdBuscado") long id);
}
