package com.example.mySchedule.controllers;

import com.example.mySchedule.models.appointmentModel;
import com.example.mySchedule.services.appoServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/appo")
@CrossOrigin("*")
public class appoControl {

    @Autowired
    appoServices myService;


    @PostMapping(value="/add")
    public appointmentModel setAppointment(@RequestBody appointmentModel newAppo){
        return myService.setAppoint(newAppo);
    }



}
