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

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
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

    //Salvar una cita nueva, si no hay otra ya
    public List<appointmentModel> setAppoint(appointmentModel newAppo){
        List<appointmentModel> myList = myRepoAppo.findRepeatedAppoHour(newAppo);
        if(myList.size()==0){
            myRepoAppo.save(newAppo);
            myList.add(newAppo);
            return  myList;
        }
        return myList;
    }

    //Cambiar una cita
    public List<appointmentModel> updateAppoint(appointmentModel newAppo){
        try{
            //verificar que existe la tarea
            appointmentModel oldAppo=myRepoAppo.findById(newAppo.getId()).get();

            //Recoger la lista de citas con horario incompatible con la nueva cita
            List<appointmentModel> myList = myRepoAppo.findRepeatedAppoHour(newAppo);

            //Si no hay horarios incompatibles
            if(myList.size()==0){
                //Hacer los cambios
                oldAppo.setAppoDate(newAppo.getAppoDate());
                oldAppo.setAppoStart(newAppo.getAppoStart());
                oldAppo.setNotes(oldAppo.getNotes()+
                        "\n"+
                        "Se cambia la cita del "+oldAppo.getAppoDate()+" al "+newAppo.getAppoDate()+" el día "+LocalDate.now()+
                        "\n"+
                        newAppo.getNotes());
                myRepoAppo.save(oldAppo);
                myList.add(oldAppo);
            }
            return myList;
        }
        catch(Exception e){
            return null;
        }
    }

    public String deleteAppo(long id) {
        try{
            //verificar que existe la tarea
            appointmentModel theAppo=myRepoAppo.findById(id).get();

            myRepoAppo.deleteById(id);

            return "La cita de "+theAppo.getUserID().getAlias()+
                    " para el "+theAppo.getAppoDate()+
                    " a las "+theAppo.getAppoStart()+
                    " se ha borrado satisfactoriamente.";

        }
        catch(Exception e){
            return "Hubo un error y no se pudo borrar: "+e.getMessage();
        }
    }
}
