package com.example.mySchedule.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
public class DTOBasicInfo implements Comparable<DTOBasicInfo> {

    private long id;
    private String name;
    private String alias;
    private String phone;
    private long appoId;
    private LocalDate nextAppoDate;
    private LocalTime nextAppoStart;
    private boolean newUser=false;
    private String feedback;

    @Override
    public int compareTo(DTOBasicInfo o) {
        LocalDate thisDate = this.getNextAppoDate();
        LocalDate otherDate = o.getNextAppoDate();

        if (thisDate == null && otherDate == null) {
            return 0; // Ambos son nulos, considerar iguales
        } else if (thisDate == null) {
            return 1; // this es nulo, considerar mayor que o
        } else if (otherDate == null) {
            return -1; // o es nulo, considerar menor que this
        } else {
            // Ninguna fecha es nula, realizar la comparación de LocalDate
            int dateComparison = thisDate.compareTo(otherDate);

            if (dateComparison == 0) {
                // Si las fechas son iguales, compara por otros criterios
                // Por ejemplo, si las fechas son iguales, compara por hora
                return this.getNextAppoStart().compareTo(o.getNextAppoStart());
            } else {
                // Si las fechas son diferentes, retorna la comparación de fechas
                return dateComparison;
            }
        }
    }
}
