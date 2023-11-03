package com.example.mySchedule.controllers;

//-------------------------|0|----------------------
// Esta clase establece los metodos para la interaccion de una llamada http
// con la base de datos.
//
// @Author: Daniel Crespo Rodriguez
// @Date: sept'23
//-------------------------->o<----------------------

import com.example.mySchedule.DTOs.DTOBasicInfo;
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

    //Devuelve todos los usuarios y la SIGUIENTE cita de cada uno (DTO)
    @GetMapping
    public ArrayList<DTOBasicInfo> readDB(){
        return myService.readUsers();
    }

    //Devuelve el usuario seleccionado con sus citas
    @GetMapping(path="/{id}")
    public userModel readAUser(@PathVariable long id){
        return myService.readAUser(id);
    }

    //Salva un nuevo usuario
    @PostMapping(value="/add")
    public String saveUser(@RequestBody userModel newUser){
        return myService.saveUser(newUser);
    }

    //Borra un usuario
    @DeleteMapping(path="/delete/{id}")
    public String deleteUser(@PathVariable long id){
        return myService.deleteUser(id);
    }

    //Edita un usuario
    @PutMapping(path="/update/{id}")
    public String changeUser(@PathVariable long id, @RequestBody userModel newUser){
        return myService.changeUser(id, newUser);
    }
}
