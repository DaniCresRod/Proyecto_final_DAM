package com.example.mySchedule.services;

import com.example.mySchedule.models.appointmentModel;
import com.example.mySchedule.models.userModel;
import com.example.mySchedule.repositories.RepoAppointment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class appoServices {
    @Autowired
    RepoAppointment myRepoAppo;

    public List<appointmentModel> readAppoints(LocalDate date){
        List<appointmentModel> milista=myRepoAppo.findRepeatedAppoDate(date);
        return milista;
    }
    //public appointmentModel readOneAppoint(userModel theUser){
    //    return myRepoAppo.findById(id);
    //}

    public appointmentModel setAppoint(appointmentModel newAppo){
        return myRepoAppo.save(newAppo);
    }

    public appointmentModel upgradeAppoint(appointmentModel newAppo){
        //verificar que existe la tarea previamente
        myRepoAppo.findById(newAppo.getId());
        //verificar que no se repite

        //guardar la tarea

        return myRepoAppo.save(newAppo);
    }
}
