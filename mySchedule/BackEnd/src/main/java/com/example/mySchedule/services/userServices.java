package com.example.mySchedule.services;


import com.example.mySchedule.DTOs.DTOBasicInfo;
import com.example.mySchedule.models.appointmentModel;
import com.example.mySchedule.models.userModel;
import com.example.mySchedule.repositories.RepoAppointment;
import com.example.mySchedule.repositories.RepoUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.time.temporal.ChronoUnit.*;

@Service
public class userServices{
    @Autowired
    RepoUser myRepo;
    @Autowired
    RepoAppointment AppoRepo;
    @Autowired
    PasswordEncoder passwordEncoder;

    //Devuelve los detalles basicos (primera cita) de todos los usuarios
    public ArrayList<DTOBasicInfo> readUsers() {
        ArrayList<DTOBasicInfo> basicInfo = new ArrayList<>();
        for (userModel eachUser : (ArrayList<userModel>) myRepo.findAll()){

            if(eachUser.getRol()== userModel.UserType.Usuario){
                long difference=-1000000000;
                LocalDate nextDate=null;
                LocalTime nextDateStart=null;
                long appoId=-1;

                for (appointmentModel eachAppo: eachUser.getAppointmentsList()){
                    LocalDateTime candidateAppo=LocalDateTime.of(eachAppo.getAppoDate(), eachAppo.getAppoStart());

                    if((!candidateAppo.isBefore(LocalDateTime.now())) && (DAYS.between(eachAppo.getAppoDate(),LocalDate.now())>difference)){
                        difference=DAYS.between(eachAppo.getAppoDate(),LocalDate.now());
                        nextDate=eachAppo.getAppoDate();
                        nextDateStart=eachAppo.getAppoStart();
                        appoId= eachAppo.getId();
                    }
                }

                DTOBasicInfo userBasicInfo=new DTOBasicInfo(eachUser.getId(),eachUser.getName(), eachUser.getAlias(), eachUser.getPhone(), appoId, nextDate, nextDateStart, false, "" );
                basicInfo.add(userBasicInfo);

            }
        }
        Collections.sort(basicInfo);
        return basicInfo;
    }

    public DTOBasicInfo saveUser(userModel newUser) {
        String feedbackMsg;
        //Se revisa si ya existe el usuario en la base de datos (Por Email)
        userModel retrievedUser=myRepo.findByEmail(newUser.getEmail());
        if(retrievedUser==null){
            //Se revisa si ya existe el usuario en la base de datos (Por NIF)
            retrievedUser=myRepo.findByUserNif(newUser.getNif()).orElse(null);
            if(retrievedUser==null){
                try{
                    //Se encripta la contraseña
                    String encodedPassword = passwordEncoder.encode(newUser.getPassword());
                    newUser.setPassword(encodedPassword);

                    //Se asigna por defecto el tipo de rol de usuario a "Usuario"
                    if(newUser.getRol()==null) newUser.setRol(userModel.UserType.Usuario);

                    retrievedUser=myRepo.save(newUser);

                    DTOBasicInfo sendUserBasicInfo= new DTOBasicInfo(retrievedUser.getId(),
                            retrievedUser.getName(),
                            retrievedUser.getAlias(),
                            retrievedUser.getPhone(),
                            -1, null, null,
                            true,
                            "El usuario "+retrievedUser.getAlias()+"se añadió con éxito");

                    return sendUserBasicInfo;
                }
                catch(Exception e){
                    e.printStackTrace();
                    return null;
                }
            }else feedbackMsg="El NIF "+newUser.getNif()+" ya esta asignado a "+retrievedUser.getName()+" "+retrievedUser.getAlias();

        }else feedbackMsg= "El email "+newUser.getEmail()+" ya esta asignado a "+retrievedUser.getName()+" "+retrievedUser.getAlias();

        return new DTOBasicInfo(
                retrievedUser.getId(), null, retrievedUser.getAlias(), retrievedUser.getPhone(),
                -1, null, null, false,
                feedbackMsg);
    }

    public String deleteUser(long id) {
        if(myRepo.existsById(id)){
            try{
                userModel myUser=myRepo.findById(id).orElse(null);
                myRepo.deleteById(id);
                return "Usuario "+myUser.getName()+
                        " "+myUser.getSurname1()+" eliminado";
            }
            catch(Exception e){
                e.printStackTrace();
                return "No se pudo borrar";
            }
        }
        return "El usuario no existe o no se encuentra";
    }

    public String changeUser(long id, userModel newUser) {
        try{
            userModel myUser = myRepo.findById(id).get();
//            myUser.setId(newUser.getId());
            myUser.setNif(newUser.getNif());
            myUser.setName(newUser.getName());
            myUser.setSurname1(newUser.getSurname1());
            myUser.setSurname2(newUser.getSurname2());
            myUser.setAlias(newUser.getAlias());
            myUser.setEmail(newUser.getEmail());
            myUser.setPhone(newUser.getPhone());
            if(newUser.getPassword()!=null) myUser.setPassword(newUser.getPassword());
            myUser.setNotes(newUser.getNotes());
            myUser.setPrice(newUser.getPrice());

            myRepo.save(myUser);

            return "Usuario "+myUser.getName()+" "+myUser.getSurname1()+" fue modificado satisfactoriamente";

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

    public ArrayList<DTOBasicInfo> readUsersAtDate(LocalDate date) {
        ArrayList<DTOBasicInfo> basicInfo = new ArrayList<>();
        LocalDate startDate= findMondayInTheWeekOf(date);
        LocalDate finishDate= startDate.plusDays(6);

        List<appointmentModel> miArray= AppoRepo.findAppoBetweenDates(startDate, finishDate);

        for (appointmentModel eachAppo : miArray){
            userModel theUser=myRepo.findById(eachAppo.getUserID().getId()).orElse(null);

            DTOBasicInfo userBasicInfo=new DTOBasicInfo(theUser.getId(),theUser.getName(), theUser.getAlias(), theUser.getPhone(), eachAppo.getId(), eachAppo.getAppoDate(), eachAppo.getAppoStart(), false, null );
            basicInfo.add(userBasicInfo);
        }
        return basicInfo;
    }

    //Busca el comienzo de la semana (Lunes) de una fecha dada
    private LocalDate findMondayInTheWeekOf(LocalDate date) {
        LocalDate startDay=date;
        switch(date.getDayOfWeek()){
            case MONDAY :
                break;
            case TUESDAY:
                startDay=date.minusDays(1);
                break;
            case WEDNESDAY:
                startDay=date.minusDays(2);
                break;
            case THURSDAY:
                startDay=date.minusDays(3);
                break;
            case FRIDAY:
                startDay=date.minusDays(4);
                break;
            case SATURDAY:
                startDay=date.minusDays(5);
                break;
            case SUNDAY:
                startDay=date.minusDays(6);
                break;
        }
        return startDay;
    }
}
