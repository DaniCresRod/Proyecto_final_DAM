package com.example.mySchedule.repositories;

import com.example.mySchedule.models.userModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepoUser extends JpaRepository<userModel, Long> {
}
