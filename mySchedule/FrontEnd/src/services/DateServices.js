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

    setTextAsTime(formattedString){
        let [hour, min]=(formattedString).split(':');

        let myHour=new Date();

        myHour.setHours(hour, min, 0);
        
        return myHour;
    },

    setTimeAsText(theDate){
        return theDate.toLocaleTimeString([], {hour: '2-digit', minute:'2-digit'});
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
    },

    getWeekDaysArray(date){
        let rangeDown;
        let weekArray=[];

        switch(this.getDayFromDate(date)){
            case "Lunes": 
            rangeDown=0;
            break;
        case "Martes":
            rangeDown=1;
            break;
        case "Miercoles":
            rangeDown=2;
            break;
        case "Jueves":
            rangeDown=3;
            break;
        case "Viernes":
            rangeDown=4;
            break;
        case "Sabado":
            rangeDown=5;
            break;
        case "Domingo":
            rangeDown=6;
            break;
        }

        for(let i=0;i<7;i++){
            let theDay=this.sumDays(date, -rangeDown+i);
            weekArray[i]=(theDay.getDate()+" / "
                        +(theDay.getMonth()+1));
        }
        return weekArray;
    },

    sumDays(date, howManyDays){
        const newDate = new Date(date);
        newDate.setDate(newDate.getDate() + howManyDays);
        return newDate;
    }

})