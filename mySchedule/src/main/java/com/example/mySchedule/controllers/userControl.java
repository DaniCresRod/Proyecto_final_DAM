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
        return myService.readUsers();
    }

    @PostMapping(value="/add")
    public userModel setUser(@RequestBody userModel newAppoint){
        return myService.setUser(newAppoint);
    }

    @DeleteMapping(path="/delete/{id}")
    public String deleteAppoint(@PathVariable long id){
        return myService.deleteUser(id);
    }

    @PutMapping(path="/update/{id}")
    public userModel changeAppoint(@PathVariable long id, @RequestBody userModel newAppoint){

        return myService.setUser(myService.changeUser(id, newAppoint));
    }

}
