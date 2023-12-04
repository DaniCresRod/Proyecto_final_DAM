import DateServices from "./DateServices";
import moment from 'moment';


const today=new Date(Date.now());

let numberOfToday=today.getDay();

function getNumberOfDaysInMonth(theDate){
    const theMonth=theDate.getMonth();
    const theYear=theDate.getFullYear();

    return new Date(theYear, theMonth, 0).getDate();
}

//Define un array rellenado con los dias de la semana y los horarios
export function defineCalendarBasics(sessionMinutes, timeWindows, customTimeArray){    
    
    let weeklyArray2=[];
    let weeklyArray=["Inicio", "Fin", "Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado", "Domingo"];
            
    if(customTimeArray===null){
        let startWorkHour=DateServices.setTextAsTime(timeWindows[0]);
        let finishWorkHour=DateServices.setTextAsTime(timeWindows[timeWindows.length-1]);

        let workingMinutes = (finishWorkHour.getTime()-startWorkHour.getTime())/(1000*60); //Minutos entre una hora y otra
        let workingLapsos=Math.ceil(workingMinutes/sessionMinutes);
        
        weeklyArray[9]=DateServices.setTimeAsText(startWorkHour);
        weeklyArray[10]=DateServices.setTimeAsText(new Date((
            (DateServices.setTextAsTime(weeklyArray[(9)])).getTime()
            )+sessionMinutes*60*1000));

        for(let i=2; i<=workingLapsos; i++){

            weeklyArray[i*9] = DateServices.setTimeAsText(
                new Date((
                    (DateServices.setTextAsTime(weeklyArray[(i*9)-9])).getTime()
                    )+sessionMinutes*60*1000)
            );

            weeklyArray[(i*9)+1] = DateServices.setTimeAsText(
                new Date((
                    (DateServices.setTextAsTime(weeklyArray[(i*9)])).getTime()
                    )+sessionMinutes*60*1000)
            );
        }
    }

    weeklyArray.forEach((item,index)=>{
        weeklyArray2[index]=new CalendarDay(index, item);
    });

    do{   
        weeklyArray2.push("");
    } while(weeklyArray2.length % 9 !== 0  );

    return(weeklyArray2);
}

//Clase padre para los objetos que iran en las casillas del calendario
class CalendarDay{
    constructor(index, tag){
        this.index=index;
        this.tag=tag;       
    }
}

//Clase que hereda de CalendarDay para las citas
export class CalendarDayBooked extends CalendarDay{
    
    constructor(index, currentDay, appoTime, userId, user, userAlias){
        super(index, user+" "+userAlias);
        this.currentDay=currentDay
        this.appoTime=appoTime;
        this.userId=userId;
        this.user=user;
        this.userAlias=userAlias;
    }
}

//Uso por primera vez la libreria moment.js (https://momentjs.com/)
export function appoIsInRange(dateFilter, userAppoDate){
    const weekDayName = DateServices.getDayFromDate(dateFilter);
    let rangeUp;
    let rangeDown;

    switch (weekDayName){
        case "Lunes": 
            rangeUp=6;
            rangeDown=0;
            break;
        case "Martes":
            rangeUp=5;
            rangeDown=1;
            break;
        case "Miercoles":
            rangeUp=4;
            rangeDown=2;
            break;
        case "Jueves":
            rangeUp=3;
            rangeDown=3;
            break;
        case "Viernes":
            rangeUp=2;
            rangeDown=4;
            break;
        case "Sabado":
            rangeUp=1;
            rangeDown=5;
            break;
        case "Domingo":
            rangeUp=0;
            rangeDown=6;
            break;
    }
    let userDate=moment(userAppoDate);
    let selectedDate=moment(dateFilter);
    console.log(selectedDate.format('YYYY-MM-DD'));
    console.log(userDate.format('YYYY-MM-DD'));
    console.log(rangeUp);
    // console.log(selectedDate.add(rangeUp,'days').format('YYYY-MM-DD'));
    // console.log(selectedDate.subtract(rangeDown, 'days').format('YYYY-MM-DD'));

    if(userDate.isBetween(selectedDate.clone().subtract(rangeDown, 'days').format('YYYY-MM-DD'), selectedDate.clone().add(rangeUp,'days').format('YYYY-MM-DD'), null, [])){
       
        return true;
    } 
    else return false;
}