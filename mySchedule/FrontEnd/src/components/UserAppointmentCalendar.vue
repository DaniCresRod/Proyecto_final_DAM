<script setup>
import { ref, watch, onBeforeMount } from 'vue';
import { myUserStore } from '../services/PiniaServices';
import DataServices from '../services/DataServices';
import DateServices from '../services/DateServices';
import {defineCalendarBasics, appoIsInRange, getIndexInMyWeeklyArray, CalendarDayBooked} from '../services/GraphCalendarServices';
import {sendWhatsApp} from '../services/WhatsAppService';
import PopUpMenuComponent from '../components/PopUpMenuComponent.vue';

const myStore=myUserStore();

const myWeeklyArray=ref([]);
const oddApposArray=ref([]);
const changedDateArray=ref([]);
const datesInWeekArray=ref([]);
// const draggedStartInfo=ref();
let draggedStartInfo;

const dateNow = new Date(Date.now());
const dateFilter = ref(dateNow.getFullYear() + "-" 
+ (dateNow.getMonth() + 1).toString().padStart(2,'0') + "-" 
+ dateNow.getDate().toString().padStart(2,'0'));

console.log(DateServices.getWeekDaysArray(dateFilter.value));

//Construye el array que se mostrará en el calendario
async function buildCalendarArray(theDate){
    try{
        oddApposArray.value=[];
        myWeeklyArray.value=[null];

        let sessionMinutes=90;
        let timeWindows=["08:00", "21:00"];

        myWeeklyArray.value=defineCalendarBasics(sessionMinutes, timeWindows, null);
        datesInWeekArray.value=DateServices.getWeekDaysArray(dateFilter.value);

        let arrayOfUsers=await DataServices.getAllUsersInTheWeekOf(theDate);
        
        arrayOfUsers.data.forEach((eachUser)=>{    
            let isInWeek=appoIsInRange(dateFilter.value,eachUser.nextAppoDate);

            if (isInWeek){
                let myIndex=getIndexInMyWeeklyArray(eachUser, myWeeklyArray.value);
                let userWithAppoThisWeek = new CalendarDayBooked(myIndex,eachUser.nextAppoDate, eachUser.nextAppoStart, eachUser.id, eachUser.name, eachUser.alias, eachUser.appoId, eachUser.phone);

                if(myIndex>-1){        

                    myWeeklyArray.value[myIndex]=userWithAppoThisWeek;
                }
                else{
                    oddApposArray.value.push(userWithAppoThisWeek);
                }          
            }              
        });

    }
    catch{
        console.log("Se produjo un error al crear el calendario");
    }    
}

function resetDate(){
  dateFilter.value=dateNow.getFullYear() + "-" 
+ (dateNow.getMonth() + 1).toString().padStart(2,'0') + "-" 
+ dateNow.getDate().toString().padStart(2,'0');
}

//Define lo que pasa cuando cogemos un elemento para arrastrarlo
function dragStart(event){console.log(event);
    //Filtramos solo los elementos que tengan datos de usuarios
    if((event.target.firstElementChild!==null) && (event.target.__vnode.key).userId ){
        draggedStartInfo=event.target.__vnode.key;  
        console.log(draggedStartInfo);
        myStore.whatsAppUser.name=draggedStartInfo.user;
        myStore.whatsAppUser.oldAppoDate=draggedStartInfo.appoDay;
        myStore.whatsAppUser.oldAppoStart=draggedStartInfo.appoTime;
        myStore.whatsAppUser.phone=draggedStartInfo.phone;      
    }
    else{
        event.preventDefault();
    }
}

function getNewDayAndHour(index, draggedIndex){
    let newHourIndex=(Math.floor(index/9))*9;
    let newDayIndex=index-newHourIndex;

    let draggedHourIndex=(Math.floor(draggedIndex/9))*9;
    let draggedDayIndex=draggedIndex-draggedHourIndex;

    return [myWeeklyArray.value[newHourIndex].tag, DateServices.sumDays(myWeeklyArray.value[draggedIndex].appoDay,newDayIndex-draggedDayIndex).toISOString().split('T')[0]];
}

async function dragEnd(event){
//El lugar de aterrizaje debe estar vacio
if((event.target.firstElementChild===null) && (event.target.innerText==="") ){

    let newIndex=getChildIndex(event.target);
    let [newHour, newDay]=getNewDayAndHour(newIndex, draggedStartInfo.index);

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

    //Si devuelve el mismo id de cita es que se ha hecho el cambio, si no es que no se ha hecho
    //Ojo porque lo que viene es una lista de citas
    //Si todo ha ido bien se puede enviar el whatsapp

    if (changedDateArray.value[0].id===dataToSend.id){
        myStore.whatsAppUser.newAppoDate = changedDateArray.value[0].appoDate;
        myStore.whatsAppUser.newAppoStart = changedDateArray.value[0].appoStart;
        document.getElementById("div_whatsapp").classList.remove("invisible");
        document.querySelector('#div_whatsapp span').textContent=`Avisar del cambio a ${draggedStartInfo.user} por WhatsApp`;
    }

}
else if(event.target.innerText!==""){
    myStore.whatsAppUser={};
    window.alert("No puedes asignar una cita ahi");
}
    // console.log(getChildIndex(event.target));
}

function getChildIndex(myNode){
    let i=0;
    while((myNode=myNode.previousElementSibling) != null) i++;
    return i;
}

function showContextMenu(event){
    // event.stopPropagation();

    let popUpMenu=document.getElementById("div_contextMenu");

    function hideMenuOnTouchMove() {
        popUpMenu.classList.add("invisible");
        document.removeEventListener("touchmove", hideMenuOnTouchMove, {passive: true});
    }

    // if(((event.target.firstElementChild!==null) && (event.target.__vnode.key).userId) || ((event.target.firstElementChild===null) 
    // && typeof((event.target.parentNode.__vnode.key).userId)!=='undefined')){
    const target = event.target;
    const parentNode = target.parentNode;
    console.log(event);

    if (
        (target.firstElementChild !== null && target.__vnode.key && target.__vnode.key.userId) ||
        (target.firstElementChild === null && parentNode && parentNode.__vnode.key && parentNode.__vnode.key.userId)
    ) {
        
        popUpMenu.classList.toggle("invisible");

        //No ha habido forma de evitar que el span dentro del p no aparezca como target del evento, asi que lo reviso
        //para acceder siempre al <p> padre.
        let myClickedNode = getChildIndex(event.target.childElementCount===0 ? event.target.parentNode : event.target);
        
        //Obtengo los datos del usuario y los almaceno en pinia
        document.getElementById("div_whatsapp").classList.add("invisible");
            myStore.whatsAppUser={};
            myStore.whatsAppUser.name=myWeeklyArray.value[myClickedNode].user;
            myStore.whatsAppUser.oldAppoDate=myWeeklyArray.value[myClickedNode].appoDay;
            myStore.whatsAppUser.oldAppoStart=myWeeklyArray.value[myClickedNode].appoTime;
            myStore.whatsAppUser.phone=myWeeklyArray.value[myClickedNode].phone;
            myStore.whatsAppUser.userId=myWeeklyArray.value[myClickedNode].userId;
            myStore.whatsAppUser.appoId=myWeeklyArray.value[myClickedNode].appoId; 
        

        if(typeof(event.touches)!=='undefined'){
            popUpMenu.style.top = event.touches[0].clientY + "px";
            popUpMenu.style.left = event.touches[0].clientX + "px";

            document.addEventListener("touchmove", hideMenuOnTouchMove, {passive: true});
        
        }
        else{
            popUpMenu.style.top=event.clientY+"px";
            popUpMenu.style.left=event.clientX+"px";
            popUpMenu.addEventListener("mouseleave", ()=>{
            popUpMenu.classList.add("invisible");
            })
        }
        
    }
      
}

watch(()=> dateFilter.value, ()=>{
    buildCalendarArray(dateFilter.value);
}),
onBeforeMount(() => {
    buildCalendarArray(dateFilter.value);
})

</script>

<template>
    <div id="div_whatsapp" class="invisible">
        <span @click="sendWhatsApp()">Avisar por whatsApp</span>
    </div>
    <section>
        <div id="div_searchBox">
            <input class="searchBox" type="date"  v-model="dateFilter"/>
            <span class="resetX" @click="resetDate()">x</span>
        </div>

        <div id="div_daysInWeek" class="div_weekGrid">
            <p></p>
            <p></p>
            <p v-for="item in datesInWeekArray" :key="item">{{ item }}</p>
        </div>
        
        <div id="div_calendar" class="div_weekGrid">            
            <p v-for="(item,index) in myWeeklyArray" :key="item" draggable="true" :id="index"
            @drop="dragEnd" @dragstart="dragStart" @dragover.prevent @dragenter.prevent 
            @touchstart.passive="showContextMenu"
            @contextmenu.prevent.stop="showContextMenu">
                <span v-if="myWeeklyArray[index]" >{{ myWeeklyArray[index].tag }}</span>
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
        
        <div id="div_contextMenu" class="invisible">
            <PopUpMenuComponent/>
        </div>

    </section>
    
</template>

<style scoped>
section{
    max-width: 75vw;
}

section:last-of-type{
    margin-bottom: 4vh;
}

.div_weekGrid{
    padding: 1vh 1vw;
    display: grid;
    grid-template-columns: 5vw 5vw repeat(7, 6vw);
    gap:1vh;
    padding-bottom: 0vh;
    align-items: center;
}

#div_searchBox{    
  display: flex;
  justify-content: flex-end;
  align-items: baseline;
  margin-bottom: 1vh;
  padding: 0 1vw 1vh;
  border-bottom: 7px solid  var(--color-text);
}

.div_weekGrid p:empty{
    display: block;
} 

p{border:1px solid black;
    font-size: smaller;
    font-weight: bold;
    text-align: center;
    word-wrap: none;
    overflow: hidden;
    
    color: var(--color-text);
    height: 5vh;   
    align-items: center;
    display:grid;
}

#div_daysInWeek p{
    border:none;
    color: var(--color-text2);
    font-size: 1vw;
    line-height: 0.5vw;
    overflow: visible;
    padding-bottom: 0;
    height: unset;
}

@media screen and (max-width: 430px) {
    .div_weekGrid{
        padding: 1vh 1vw;
        display: grid;
        grid-template-columns: 15vw 15vw repeat(7, 70px);
        gap:1vh;
        font-size: 5vw;      
    }

    .div_weekGrid :not(p:empty){
        align-self: center;
    }

    .div_weekGrid p:empty{
        padding: 10px 0;
    }   

    #div_daysInWeek{
    }

    #div_daysInWeek p{
        font-size: smaller;
        margin-bottom: 0;
        line-height: unset;
        padding: 0;
    }  
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

#div_whatsapp{border:5px solid var(--color-text2);
    position: fixed;
    right: 0vw;
    top:22vh;
    width: 10vw;
    min-width: 100px;
    border-radius: 100% 0 0 100%;
    padding: 1vw 2vh;
    text-align: center;
    color:var(--color-text2);
}

#div_contextMenu{
    position: absolute;
    z-index:3;
    width: min-content;
    overflow: hidden;
    padding: 0;
    margin:0;    
}

</style>