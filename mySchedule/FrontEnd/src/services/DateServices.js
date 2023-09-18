export default({

    removeSeconds(appoStartHour){
        let [hour, min]=(appoStartHour).split(':');

        let myHour=new Date();

        myHour.setHours(hour, min, 0);
        
        return myHour.toLocaleTimeString([], {hour: '2-digit', minute:'2-digit'});
    },

    getEndHour(appoStartHour){
        let [hour, min]=(appoStartHour).split(':');

        let EndHour=new Date();

        EndHour.setHours(hour, min, 0);

        EndHour.setMinutes(EndHour.getMinutes() + 75);
        
        return EndHour.toLocaleTimeString([], {hour: '2-digit', minute:'2-digit'});
    },

    changeFormatToDate(date){
        let [year, month, day]=date.split("-");
        return day+"/"+(month)+"/"+year;
    },

    getDayFromDate(date){
        let [year, month, day]=date.split("-");

        let theDay=new Date();
        theDay.setMonth(month-1, day); //Los meses empiezan en Enero=0
        theDay.setFullYear(year);

        let index=theDay.getDay();
        let SpanishDays=["Domingo", "Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado"];

        return SpanishDays[index];
    }

})