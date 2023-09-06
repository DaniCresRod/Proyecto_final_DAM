package com.example.mySchedule.services;


import com.example.mySchedule.models.userModel;
import com.example.mySchedule.repositories.Repo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;


public class userServices{
    @Autowired
    Repo myRepo;


    public ArrayList<userModel> readDB() {
        return (ArrayList<userModel>) myRepo.findAll();
    }

    public userModel setAppoint(userModel newAppoint) {
        try{
            return myRepo.save(newAppoint);
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }

    }

    public String deleteAppoint(int id) {
        try{
            myRepo.deleteById(id);
            return "record "+id+" deleted";
        }
        catch(Exception e){
            e.printStackTrace();
            return "Can not delete record";
        }
    }

    public userModel changeAppoint(int id, userModel newAppoint) {
        try{
            userModel myAppoint = myRepo.findById(id).get();

            myAppoint.setId(newAppoint.getId());
            myAppoint.setName(newAppoint.getName());
            myAppoint.setAppoDate(newAppoint.getAppoDate());
            myAppoint.setAppoStart(newAppoint.getAppoStart());
            myAppoint.setNotes(newAppoint.getNotes());

            return myAppoint;

        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
