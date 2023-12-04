import DateServices from "./DateServices";


const today=new Date(Date.now());

let numberOfToday=today.getDay();




function GetNumberOfDaysInMonth(theDate){
    const theMonth=theDate.getMonth();
    const theYear=theDate.getFullYear();

    return new Date(theYear, theMonth, 0).getDate();
}

//Define un array rellenado con los dias de la semana y los horarios
export function DefineCalendarBasics(sessionMinutes, timeWindows, customTimeArray){    
    let weeklyArray=[];
    let weeklyArray2=[];
    weeklyArray2=["Inicio", "Fin", "Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado", "Domingo"];
    
    weeklyArray2.forEach((item,index)=>{
        weeklyArray[index]=new CalendarDay(index, item);
    })
    console.log(weeklyArray);
    
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
    return(weeklyArray);
}

class CalendarDay{
    constructor(index, tag){
        this.index=index;
        this.tag=tag;       
    }
}

class CalendarDayBooked extends CalendarDay{
    
    constructor(index, currentDay, appoTime, userId, user, userAlias){
        super(index, user+" "+userAlias);
        this.currentDay=currentDay
        this.appoTime=appoTime;
        this.userId=userId;
        this.user=user;
        this.userAlias=userAlias;
    }
}