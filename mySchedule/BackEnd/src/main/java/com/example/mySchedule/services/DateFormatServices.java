package com.example.mySchedule.services;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class DateFormatServices {

    DateTimeFormatter myFormat=DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public String formatMyDate(LocalDate myDate){
        return myDate.format(myFormat);
    }
}
