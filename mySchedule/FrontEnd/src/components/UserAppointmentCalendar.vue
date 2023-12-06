<script setup>
import { ref, watch, onBeforeMount } from 'vue';
// import { myUserStore } from '../services/PiniaServices';
import DataServices from '../services/DataServices';
import {sumDays, defineCalendarBasics, appoIsInRange, getIndexInMyWeeklyArray, CalendarDayBooked} from '../services/GraphCalendarServices'

// const myStore=myUserStore();

const myWeeklyArray=ref([]);
const oddApposArray=ref([]);
const changedDateArray=ref([])
// const draggedStartInfo=ref();
let draggedStartInfo;

const dateNow= new Date(Date.now());

//Construye el array que se mostrará en el calendario
async function buildCalendarArray(theDate){
    try{
        oddApposArray.value=[];
        myWeeklyArray.value=[null];

        let sessionMinutes=90;
        let timeWindows=["08:00", "21:00"];
        myWeeklyArray.value=defineCalendarBasics(sessionMinutes, timeWindows, null);
        
        let arrayOfUsers=await DataServices.getAllUsersInTheWeekOf(theDate);
        
        arrayOfUsers.data.forEach((eachUser)=>{    
            let isInWeek=appoIsInRange(dateFilter.value,eachUser.nextAppoDate);

            if (isInWeek){
                let myIndex=getIndexInMyWeeklyArray(eachUser, myWeeklyArray.value);
                let userWithAppoThisWeek = new CalendarDayBooked(myIndex,eachUser.nextAppoDate, eachUser.nextAppoStart, eachUser.id, eachUser.name, eachUser.alias, eachUser.appoId);

                if(myIndex>-1){        

                    myWeeklyArray.value[myIndex]=userWithAppoThisWeek;
                }
                else{
                    oddApposArray.value.push(userWithAppoThisWeek);
                }          
            }              
        });
    // console.log(myWeeklyArray.value);
    // console.log(oddApposArray.value);

    }
    catch{
        console.log("liada");
    }
    
}

const dateFilter = ref(dateNow.getFullYear() + "-" 
+ (dateNow.getMonth() + 1).toString().padStart(2,'0') + "-" 
+ dateNow.getDate().toString().padStart(2,'0'));

function resetDate(){
  dateFilter.value=dateNow.getFullYear() + "-" 
+ (dateNow.getMonth() + 1).toString().padStart(2,'0') + "-" 
+ dateNow.getDate().toString().padStart(2,'0');
}

//Define lo que pasa cuando cogemos un elemento para arrastrarlo
function dragStart(event){
    //Filtramos solo los elementos que tengan datos de usuarios
    if((event.target.firstElementChild!==null) && (event.target.__vnode.key).userId ){
        draggedStartInfo=event.target.__vnode.key;        
    }
    else{
        event.preventDefault();
    }
}

async function dragEnd(event){
//El lugar de aterrizaje debe estar vacio
if((event.target.firstElementChild===null) && (event.target.innerText==="") ){
    // console.log(event.srcElement.__vnode.key);
console.log(draggedStartInfo);
    let newIndex=getChildIndex(event.target);
    let [newHour, newDay]=getNewDayAndHour(newIndex, draggedStartInfo.index);

    console.log(newHour);
    console.log(newDay);
    myWeeklyArray.value[draggedStartInfo.index]="";

    let dataToSend={
        id: draggedStartInfo.appoId,
        appoDate: newDay,
        appoStart: newHour,
        notes: "",
        userID:{
            id: draggedStartInfo.userId}
    }
    let response=await DataServices.updateAppo(JSON.stringify(dataToSend));
    changedDateArray.value=response.data;
    console.log(changedDateArray.value);
    buildCalendarArray(dateFilter.value);
    // draggedStartInfo.index=newIndex;
    // myWeeklyArray.value[newIndex]=draggedStartInfo;    

    // console.log(myWeeklyArray.value);

}
else if(event.target.innerText!==""){
    window.alert("No puedes asignar una cita ahi");
}
    // console.log(getChildIndex(event.target));
}

function getChildIndex(myNode){
    let i=0;
    while((myNode=myNode.previousElementSibling) != null) i++;
    return i;
}

function getNewDayAndHour(index, draggedIndex){
    let newHourIndex=(Math.floor(index/9))*9;
    let newDayIndex=index-newHourIndex;

    let draggedHourIndex=(Math.floor(draggedIndex/9))*9;
    let draggedDayIndex=draggedIndex-draggedHourIndex;

    return [myWeeklyArray.value[newHourIndex].tag, sumDays(myWeeklyArray.value[draggedIndex].appoDay,newDayIndex-draggedDayIndex).toISOString().split('T')[0]];
}

watch(()=> dateFilter.value, ()=>{
    console.log(dateFilter.value);
    buildCalendarArray(dateFilter.value);
}),
onBeforeMount(() => {
    buildCalendarArray(dateFilter.value);
})

</script>

<template>
    <section>
        <div id="div_searchBox">
        <input class="searchBox" type="date"  v-model="dateFilter"/>
        <span class="resetX" @click="resetDate()">x</span>
      </div>
        <div>
            <p v-for="(item,index) in myWeeklyArray" :key="item" draggable="true" @drop="dragEnd" @dragstart="dragStart" :id="index" @dragover.prevent @dragenter.prevent>
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
    /* padding-top: 1vh; */
    padding-left: 1vw;
    font-weight:normal;
}

.article_outOfSchedule{
    margin-top: 1.5vh;
}

.article_outOfSchedule .p_outOfSchedule:first-of-type{
    padding-top: 1vh;
}

</style>