<script setup>
import { ref, watch } from 'vue';
import { myUserStore } from '../services/PiniaServices';
import {defineCalendarBasics, appoIsInRange, getIndexInMyWeeklyArray, CalendarDayBooked} from '../services/GraphCalendarServices'

const myStore=myUserStore();

const myWeeklyArray=ref([]);
const oddApposArray=ref([]);

const dateNow= new Date(Date.now());

//Construye el array que se mostrará en el calendario
function buildCalendarArray(dateNow){
    oddApposArray.value=[];

    let sessionMinutes=90;
    let timeWindows=["08:00", "21:00"];
    myWeeklyArray.value=defineCalendarBasics(sessionMinutes, timeWindows, null);

    console.log(myWeeklyArray.value);

    myStore.AllUsers.forEach((eachUser)=>{
        let isInWeek=appoIsInRange(dateFilter.value,eachUser.nextAppoDate);

        if (isInWeek){
            console.log(eachUser);
            let myIndex=getIndexInMyWeeklyArray(eachUser, myWeeklyArray.value);
            let userWithAppoThisWeek = new CalendarDayBooked(myIndex,eachUser.nextAppoDate, eachUser.nextAppoStart, eachUser.id, eachUser.name, eachUser.alias);

            if(myIndex>-1){        

                myWeeklyArray.value[myIndex]=userWithAppoThisWeek;
            }
            else{
                oddApposArray.value.push(userWithAppoThisWeek);
            } 
            console.log(oddApposArray.value);           
        }
              
    })

    console.log(myWeeklyArray.value);
}

const dateFilter = ref(dateNow.getFullYear() + "-" 
+ (dateNow.getMonth() + 1).toString().padStart(2,'0') + "-" 
+ dateNow.getDate().toString().padStart(2,'0'));

function resetDate(){
  dateFilter.value=dateNow.getFullYear() + "-" 
+ (dateNow.getMonth() + 1).toString().padStart(2,'0') + "-" 
+ dateNow.getDate().toString().padStart(2,'0');
}

watch(()=> myStore.AllUsers, ()=>{
    console.log(dateNow.getMonth());
    buildCalendarArray(dateNow);
});

watch(()=> dateFilter.value, ()=>{
    console.log(dateNow.getMonth());
    buildCalendarArray(dateNow);
});

</script>

<template>
    <section>
        <div id="div_searchBox">
        <input class="searchBox" type="date"  v-model="dateFilter"/>
        <span class="resetX" @click="resetDate()">x</span>
      </div>
        <div>
            <p v-for="(item,index) in myWeeklyArray" :key="item">
                <span v-if="myWeeklyArray[index]">{{ myWeeklyArray[index].tag }}</span>
            </p>                      
        </div>
        <article class="article_outOfSchedule" v-if="oddApposArray.length>0">
            Citas en horarios especiales:
            <p class="p_outOfSchedule" v-for="(item,index) in oddApposArray" :key="item">
                {{ oddApposArray[index].tag }} 
                tiene cita el {{ oddApposArray[index].appoDay }}
                a las {{ oddApposArray[index].appoTime }}
            </p>
        </article>
    </section>
</template>

<style scoped>
section{
    max-width: 75vw;
}

div{
    padding: 1vh 1vw;
    display: grid;
    grid-template-columns: 5vw 5vw repeat(7, 6vw);
    gap:1vh;
}

#div_searchBox{    
  display: flex;
  justify-content: flex-end;
  align-items: baseline;
  margin-bottom: 2vh;

  border-bottom: 7px solid  var(--color-text);
}

p{border:1px solid black;
    font-size: smaller;
    font-weight: bold;
    text-align: center;
    
}

.p_outOfSchedule{
    border:none;
    text-align: left;
    padding-top: 1vh;
    padding-left: 1vw;
    font-weight:normal;
}

.article_outOfSchedule{
    margin-top: 1.5vh;
}

</style>