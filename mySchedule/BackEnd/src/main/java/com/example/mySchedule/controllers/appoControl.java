package com.example.mySchedule.controllers;

import com.example.mySchedule.models.appointmentModel;
import com.example.mySchedule.services.appoServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/appo")
@CrossOrigin("*")
public class appoControl {

    @Autowired
    appoServices myService;


    //Cita nueva
    @PostMapping(value="/add")
    public List<appointmentModel> setAppointment(@RequestBody appointmentModel newAppo){
        return myService.setAppoint(newAppo);
    }

    //Saber las citas que hay un dia determinado (por fecha)
    @GetMapping(path="/{date}")
    public List<appointmentModel> getApposByDate(@PathVariable String date){
        LocalDate localDate = LocalDate.parse(date);
        return myService.readAppoints(localDate);
    }

    //Cambiar una cita de fecha
    @PutMapping(path="/update")
    public List<appointmentModel> updateAppo(@RequestBody appointmentModel newAppo){
        return myService.updateAppoint(newAppo);
    }

    //Cambiar detalles de una cita (sin cambio de fecha)
    @PutMapping(path="/updateNotes")
    public appointmentModel updateAppoDetail(@RequestBody appointmentModel newAppo){
        return myService.updateAppointDetail(newAppo);
    }

    //Borrar una cita
    @DeleteMapping(path = "/delete/{id}")
    public String deleteAppo(@PathVariable long id){
        return myService.deleteAppo(id);
    }

    //Generar factura
    @PostMapping(path="/genBill/{id}")
    public String genBill(@PathVariable long id){
        return myService.generateBill(id);
    }
}
