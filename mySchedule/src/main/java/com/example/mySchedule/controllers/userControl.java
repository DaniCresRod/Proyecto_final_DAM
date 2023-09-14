package com.example.mySchedule.controllers;

import com.example.mySchedule.models.userModel;
import com.example.mySchedule.services.userServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/user")
@CrossOrigin("*")
public class userControl{

    @Autowired
    userServices myService;

    //Devuelve todos los usuarios y citas de cada uno
    @GetMapping
    public ArrayList<userModel> readDB(){
        return myService.readUsers();
    }

    //Devuelve el usuario seleccionado
    @GetMapping(path="/{id}")
    public userModel readAUser(@PathVariable long id){
        return myService.readAUser(id);
    }

    @PostMapping(value="/add")
    public String saveUser(@RequestBody userModel newUser){
        return myService.saveUser(newUser);
    }

    @DeleteMapping(path="/delete/{id}")
    public String deleteUser(@PathVariable long id){
        return myService.deleteUser(id);
    }

    @PutMapping(path="/update/{id}")
    public String changeUser(@PathVariable long id, @RequestBody userModel newUser){
        return myService.changeUser(id, newUser);
    }



}
