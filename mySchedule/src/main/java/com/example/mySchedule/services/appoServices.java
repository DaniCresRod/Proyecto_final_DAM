package com.example.mySchedule.services;

//-------------------------|0|----------------------
// Esta clase establece los metodos para la interaccion de una llamada http
// con la bas de datos.
//
// @Author: Daniel Crespo Rodriguez
// @Date: sept'23
//-------------------------->o<----------------------

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

    //Devuelve todas las citas de ese dia
    public List<appointmentModel> readAppoints(LocalDate date){
        return myRepoAppo.findAppoByDate(date);
    }

    //Salvar una cita nueva si no hay otra ya
    public List<appointmentModel> setAppoint(appointmentModel newAppo){
        List<appointmentModel> myList = myRepoAppo.findRepeatedAppoHour(newAppo.getAppoStart());
        if(myList.size()==0){
            myRepoAppo.save(newAppo);
            myList.add(newAppo);
            return  myList;
        }
        return myList;
    }

    public appointmentModel upgradeAppoint(appointmentModel newAppo){
        //verificar que existe la tarea previamente
        myRepoAppo.findById(newAppo.getId());
        //verificar que no se repite

        //guardar la tarea

        return myRepoAppo.save(newAppo);
    }
}
