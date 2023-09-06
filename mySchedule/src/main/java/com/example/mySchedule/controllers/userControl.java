package com.example.mySchedule.controllers;

import com.example.mySchedule.models.userModel;
import com.example.mySchedule.services.userServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/chart")
@CrossOrigin("*")
public class userControl{

    @Autowired
    userServices myService;

    @GetMapping
    public ArrayList<userModel> readDB(){
        return myService.readDB();
    }

    @PostMapping(value="/add")
    public userModel setAppoint(@RequestBody userModel newAppoint){

        return myService.setAppoint(newAppoint);
    }

    @DeleteMapping(path="/delete/{id}")
    public String deleteAppoint(@PathVariable int id){
        return myService.deleteAppoint(id);
    }

    @PutMapping(path="/update/{id}")
    public userModel changeAppoint(@PathVariable int id, @RequestBody userModel newAppoint){

        return myService.setAppoint(myService.changeAppoint(id, newAppoint));
    }

}
