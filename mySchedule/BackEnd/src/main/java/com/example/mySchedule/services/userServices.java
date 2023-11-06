package com.example.mySchedule.services;


import com.example.mySchedule.DTOs.DTOBasicInfo;
import com.example.mySchedule.models.appointmentModel;
import com.example.mySchedule.models.userModel;
import com.example.mySchedule.repositories.RepoUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;

import static java.time.temporal.ChronoUnit.*;

@Service
public class userServices{
    @Autowired
    RepoUser myRepo;
    @Autowired
    PasswordEncoder passwordEncoder;

    public ArrayList<DTOBasicInfo> readUsers() {
        ArrayList<DTOBasicInfo> basicInfo = new ArrayList<>();
        for (userModel eachUser : (ArrayList<userModel>) myRepo.findAll()){

            long difference=1000000;
            LocalDate nextDate=null;
            LocalTime nextDateStart=null;

            for (appointmentModel eachAppo: eachUser.getAppointmentsList()){
                if((eachAppo.getAppoDate().isAfter(LocalDate.now())) && (DAYS.between(eachAppo.getAppoDate(),LocalDate.now())<difference)){
                    difference=DAYS.between(eachAppo.getAppoDate(),LocalDate.now());
                    nextDate=eachAppo.getAppoDate();
                    nextDateStart=eachAppo.getAppoStart();
                }
            }

            DTOBasicInfo userBasicInfo=new DTOBasicInfo(eachUser.getId(),eachUser.getName(), eachUser.getAlias(), nextDate, nextDateStart );
            basicInfo.add(userBasicInfo);
        }
        Collections.sort(basicInfo);
        return basicInfo;
    }

    public String saveUser(userModel newUser) {
        //Se revisa si ya existe el usuario en la base de datos (Por Email)
        userModel retrievedUser=myRepo.findByEmail(newUser.getEmail());
        if(retrievedUser==null){
            //Se revisa si ya existe el usuario en la base de datos (Por NIF)
            retrievedUser=myRepo.findByNif(newUser.getNif());
            if(retrievedUser==null){
                try{
                    //Se encripta la contraseña
                    String encodedPassword = passwordEncoder.encode(newUser.getPassword());
                    newUser.setPassword(encodedPassword);

                    //Se asigna por defecto el tipo de rol de usuario a "Usuario"
                    if(newUser.getRol()==null) newUser.setRol(userModel.UserType.Usuario);

                    retrievedUser=myRepo.save(newUser);
                    return retrievedUser.getAlias()+" guardado con exito";
                }
                catch(Exception e){
                    e.printStackTrace();
                    return "Error: No se pudo guardar el registro";
                }
            }else return "El NIF "+newUser.getNif()+" ya esta asignado a "+retrievedUser.getAlias();
        }else return "El email "+newUser.getEmail()+" ya esta asignado a "+retrievedUser.getAlias();
    }

    public String deleteUser(long id) {
        if(myRepo.existsById(id)){
            try{
                myRepo.deleteById(id);
                return "record "+id+" deleted";
            }
            catch(Exception e){
                e.printStackTrace();
                return "Can not delete record";
            }
        }
        return "El usuario no existe o no se encuentra";
    }

    public String changeUser(long id, userModel newUser) {
        try{
            userModel myUser = myRepo.findById(id).get();
            myUser.setId(newUser.getId());
            myUser.setAlias(newUser.getAlias());
            myUser.setEmail(newUser.getEmail());
            myUser.setName(newUser.getName());
            myUser.setNotes(newUser.getNotes());
            myUser.setPassword(newUser.getPassword());

            myRepo.save(myUser);

            return myUser.getAlias()+" fue modificado satisfactoriamente";

        }
        catch(Exception e){
            e.printStackTrace();
            return "El registro no se pudo modificar";
        }
    }

    public userModel readAUser(long id) {
        if(myRepo.existsById(id)){
            return myRepo.findById(id).orElse(null);
        }
        return null;
    }
}
