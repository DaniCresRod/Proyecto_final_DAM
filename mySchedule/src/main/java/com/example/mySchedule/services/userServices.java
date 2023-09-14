package com.example.mySchedule.services;


import com.example.mySchedule.models.userModel;
import com.example.mySchedule.repositories.RepoUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class userServices{
    @Autowired
    RepoUser myRepo;

    public ArrayList<userModel> readUsers() {
        return (ArrayList<userModel>) myRepo.findAll();
    }

    public String saveUser(userModel newUser) {
        userModel retrievedUser=myRepo.findByEmail(newUser.getEmail());
        if(retrievedUser==null){

            retrievedUser=myRepo.findByNif(newUser.getNif());
            if(retrievedUser==null){
                try{
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
