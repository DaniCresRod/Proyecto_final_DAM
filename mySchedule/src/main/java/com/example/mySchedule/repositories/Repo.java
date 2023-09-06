package com.example.mySchedule.repositories;

import com.example.mySchedule.models.userModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface Repo extends JpaRepository<userModel, Integer> {
}
