import DateServices from "./DateServices";
import moment from 'moment';


// const today=new Date(Date.now());

// let numberOfToday=today.getDay();

// function getNumberOfDaysInMonth(theDate){
//     const theMonth=theDate.getMonth();
//     const theYear=theDate.getFullYear();

//     return new Date(theYear, theMonth, 0).getDate();
// }

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

    //Se completa el array si no hay usuarios en las ultimas horas
    //De no hacerse asi, se generan indices duplicados y no se renderiza
    //bien el v-for. Le estamos dando un valor nulo (si no hay cita)
    //a la que deberia ser la ultima casilla de nuestro calendario
    if(weeklyArray2[(Math.ceil(weeklyArray2.length/9)*9)-1]===undefined){        
        weeklyArray2[(Math.ceil(weeklyArray2.length/9)*9)-1]=null        
    }

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
    
    constructor(index, appoDay, appoTime, userId, user, userAlias, appoId, phone){
        super(index, user+" "+userAlias);
        this.appoDay=appoDay
        this.appoTime=DateServices.removeSeconds(appoTime);
        this.userId=userId;
        this.appoId=appoId;
        this.user=user;
        this.userAlias=userAlias;
        this.phone=phone;
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
    // console.log(selectedDate.format('YYYY-MM-DD'));
    // console.log(userDate.format('YYYY-MM-DD'));
    // console.log(rangeUp);
    // console.log(selectedDate.add(rangeUp,'days').format('YYYY-MM-DD'));
    // console.log(selectedDate.subtract(rangeDown, 'days').format('YYYY-MM-DD'));

    if(userDate.isBetween(selectedDate.clone().subtract(rangeDown, 'days').format('YYYY-MM-DD'), selectedDate.clone().add(rangeUp,'days').format('YYYY-MM-DD'), null, [])){
       
        return true;
    } 
    else return false;
}

//Esta funcion asigna indice en el array que formara el calendario a un usuario
export function getIndexInMyWeeklyArray(userWithAppo, theArray){
    const weekDayName = DateServices.getDayFromDate(userWithAppo.nextAppoDate);
    let column;
    let row;
    switch (weekDayName){
        case "Lunes": 
            column=2;
            break;
        case "Martes":
            column=3;
            break;
        case "Miercoles":
            column=4;
            break;
        case "Jueves":
            column=5;
            break;
        case "Viernes":
            column=6;
            break;
        case "Sabado":
            column=7;
            break;
        case "Domingo":
            column=8;
            break;
        default:
            column=0;
            break;
    }

    const appoTime = DateServices.removeSeconds(userWithAppo.nextAppoStart);
    
    for(let i=1;i<theArray.length/9;i++){
        if(appoTime===theArray[i*9].tag){
            row=i;
            break;
        }        
    }
    return (column)+(row*9);
}

export function sumDays(date, howManyDays){
    const newDate = new Date(date);
    newDate.setDate(newDate.getDate() + howManyDays);
    return newDate;
}

