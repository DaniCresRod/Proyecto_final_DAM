import DateServices from "./DateServices";

const today=new Date(Date.now());

let numberOfToday=today.getDay();

function GetNumberOfDaysInMonth(theDate){
    const theMonth=theDate.getMonth();
    const theYear=theDate.getFullYear();

    return new Date(theYear, theMonth, 0).getDate();
}

class CalendarDay{
    constructor(index, tag, currentDay){
        this.index=index;
        this.tag=tag;
        this.currentDay=currentDay;        
    }
}

class CalendarDayBooked extends CalendarDay{
    
    constructor(index, currentDay, appoTime, userId, user, userAlias){
        super(index, user+" "+userAlias, currentDay);
        
        this.appoTime=appoTime;
        this.userId=userId;
        this.user=user;
        this.userAlias=userAlias;
    }
}