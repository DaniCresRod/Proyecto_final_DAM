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

    public userModel setUser(userModel newAppoint) {
        try{
            return myRepo.save(newAppoint);
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public String deleteUser(long id) {
        try{
            myRepo.deleteById(id);
            return "record "+id+" deleted";
        }
        catch(Exception e){
            e.printStackTrace();
            return "Can not delete record";
        }
    }

    public userModel changeUser(long id, userModel newAppoint) {
        try{
            userModel myUser = myRepo.findById(id).get();
            myUser.setId(newAppoint.getId());
            myUser.setAlias(newAppoint.getAlias());
            myUser.setEmail(newAppoint.getEmail());
            myUser.setName(newAppoint.getName());
            myUser.setNotes(newAppoint.getNotes());
            myUser.setPassword(newAppoint.getPassword());

            return myUser;

        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
