<script setup>
import { ref, watch, onBeforeMount } from 'vue';
import { myUserStore } from '../services/PiniaServices';
import DataServices from '../services/DataServices';
import DateServices from '../services/DateServices';
import {defineCalendarBasics, appoIsInRange, getIndexInMyWeeklyArray, CalendarDayBooked, getChildIndex, getNewDayAndHour} from '../services/GraphCalendarServices';
import {sendWhatsApp, sendReminder} from '../services/WhatsAppService';
import PopUpMenuComponent from './ContextMenu/PopUpMenuComponent.vue';

const myStore=myUserStore();

const myWeeklyArray=ref([]);
const oddApposArray=ref([]);
const changedDateArray=ref([]);
const datesInWeekArray=ref([]);
const whatsAppMsj=ref();
let draggedStartInfo;
const indexToHighlight=ref();

const dateNow = new Date(Date.now());
const dateFilter = ref(dateNow.getFullYear() + "-" 
+ (dateNow.getMonth() + 1).toString().padStart(2,'0') + "-" 
+ dateNow.getDate().toString().padStart(2,'0'));

//Construye el array que se mostrará en el calendario
async function buildCalendarArray(theDate){
    try{
        oddApposArray.value=[];
        myWeeklyArray.value=[null];

        let sessionMinutes=90;
        let timeWindows=["08:00", "21:00"];

        myWeeklyArray.value=defineCalendarBasics(sessionMinutes, timeWindows, null);
        datesInWeekArray.value=DateServices.getWeekDaysArray(dateFilter.value);

        //Modificacion posterior para marcar el dia actual
        indexToHighlight.value=datesInWeekArray.value.pop();
        
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
            
        highlight(indexToHighlight.value);
        
    }
    catch{
        console.log("Se produjo un error al crear el calendario");
    }    
}

function highlight(indexToHighlight){
    const cuadro=document.querySelectorAll("#div_calendar>p");
        cuadro.forEach(eachP=>eachP.classList.remove('highlight'))
        
        cuadro[indexToHighlight+2].classList.add('highlight');
}

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

        myStore.whatsAppUser.name=draggedStartInfo.user;
        myStore.whatsAppUser.oldAppoDate=draggedStartInfo.appoDay;
        myStore.whatsAppUser.oldAppoStart=draggedStartInfo.appoTime;
        myStore.whatsAppUser.phone=draggedStartInfo.phone;      
    }
    else{
        event.preventDefault();
    }
}

function dragEnd(event){
    //El lugar de aterrizaje debe estar vacio
    if((event.target.firstElementChild===null) && (event.target.innerText==="") ){
        let newIndex=getChildIndex(event.target);
        let [newHourIndex, draggedDayIndex, newDayIndex]=getNewDayAndHour(newIndex, draggedStartInfo.index);
        let [newHour, newDay]=[myWeeklyArray.value[newHourIndex].tag, DateServices.sumDays(myWeeklyArray.value[draggedStartInfo.index].appoDay,newDayIndex-draggedDayIndex).toISOString().split('T')[0]]

        let dataToSend={
            id: draggedStartInfo.appoId,
            appoDate: newDay,
            appoStart: newHour,
            notes: "",
            userID:{
                id: draggedStartInfo.userId}
        }
        executeAppoChange(dataToSend);
    }
    else if(event.target.innerText!==""){
        myStore.whatsAppUser={};
        myStore.onChanging=false;
        window.alert("No puedes asignar una cita ahi");
    }
        // console.log(getChildIndex(event.target));
}

function showContextMenu(event){
    // event.stopPropagation();
    cancelAppoMove();

    let popUpMenu=document.getElementById("div_contextMenu");

    function hideMenuOnTouchMove() {
        popUpMenu.classList.add("invisible");
        document.removeEventListener("touchmove", hideMenuOnTouchMove, {passive: true});
    }
    
    const target = event.target;
    const targetParentNode = target.parentNode;

    if ((target.firstElementChild !== null && target.__vnode.key && target.__vnode.key.userId) ||
        (target.firstElementChild === null && targetParentNode && targetParentNode.__vnode.key && targetParentNode.__vnode.key.userId)
    ) {        
        popUpMenu.classList.toggle("invisible");

        //No ha habido forma de evitar que el span dentro del p no aparezca como target del evento, asi que lo reviso
        //para acceder siempre al <p> padre.
        let myClickedNode = getChildIndex(event.target.childElementCount===0 ? event.target.parentNode : event.target);

        //Se resalta el elemento seleccionado
        let myDocument=document.querySelector(':root');
        let myColor=getComputedStyle(myDocument).getPropertyValue('--color-border');
        document.querySelector(`#div_calendar p:nth-of-type(${myClickedNode+1})`).style.border=`3px solid ${myColor}`;
        
        //Obtengo los datos del usuario y los almaceno en pinia
        document.getElementById("div_whatsapp").classList.add("invisible");
        
        myStore.whatsAppUser={};
        myStore.whatsAppUser.name=myWeeklyArray.value[myClickedNode].user;
        myStore.whatsAppUser.oldAppoDate=myWeeklyArray.value[myClickedNode].appoDay;
        myStore.whatsAppUser.oldAppoStart=myWeeklyArray.value[myClickedNode].appoTime;
        myStore.whatsAppUser.phone=myWeeklyArray.value[myClickedNode].phone;
        myStore.whatsAppUser.userId=myWeeklyArray.value[myClickedNode].userId;
        myStore.whatsAppUser.appoId=myWeeklyArray.value[myClickedNode].appoId; 
        myStore.whatsAppUser.indexOfArray=myClickedNode;

        if(typeof(event.touches)!=='undefined'){
            popUpMenu.style.top = event.touches[0].clientY + "px";
            popUpMenu.style.left = event.touches[0].clientX +10+ "px";

            document.addEventListener("touchmove", hideMenuOnTouchMove, {passive: true});
        
        }
        else{
            popUpMenu.style.top=event.clientY+"px";
            popUpMenu.style.left=event.clientX+"px";
            popUpMenu.addEventListener("mouseleave", ()=>{
                popUpMenu.classList.add("invisible");});
        }        
    }      
}

function changeWeekAppointment(event){
    if(myStore.onChanging){
        myUserStore().onChanging=false;

        //El lugar de aterrizaje debe estar vacio
        if((event.target.firstElementChild===null) && (event.target.innerText==="") ){
            
            let newIndex=getChildIndex(event.target);
            let oldIndex=myUserStore().whatsAppUser.indexOfArray;
        
            // eslint-disable-next-line no-unused-vars
            let [newHourIndex, draggedDayIndex, newDayIndex]=getNewDayAndHour(newIndex, oldIndex);
            let [newHour, newDay]=[myWeeklyArray.value[newHourIndex].tag, datesInWeekArray.value[newDayIndex-2]];
            
            newDay=newDay.split("/");

            //ajustar fecha y formato teniendo cuidado con los cambios de año
            let parsedDate = new Date(Date.parse(dateFilter.value));
            if(parsedDate.getMonth()===11 && newDay[1].trim()=="01"){
                newDay=(parsedDate.getFullYear()+1)+"-"+newDay[1].trim()+"-"+newDay[0].trim();
            }
            else if(parsedDate.getMonth()===0 && newDay[1].trim()=="12"){
                newDay=(parsedDate.getFullYear()-1)+"-"+newDay[1].trim()+"-"+newDay[0].trim();
            }
            else{
                newDay=(parsedDate.getFullYear())+"-"+newDay[1].trim()+"-"+newDay[0].trim();
            }

            let dataToSend={
                id: myStore.whatsAppUser.appoId,
                appoDate: newDay,
                appoStart: newHour,
                notes: "",
                userID:{
                    id: myStore.whatsAppUser.userId}
            };
            if(dataToSend.id!==""){
                executeAppoChange(dataToSend);
            }
            else{
                executeNewAppo(dataToSend);
            }
        } 
        else{
            cancelAppoMove(); 
        }         
    }     
}

async function executeNewAppo(dataToSend){
    let response=await DataServices.saveAppo(JSON.stringify(dataToSend));
    changedDateArray.value=response.data;
    buildCalendarArray(dateFilter.value);

    cancelAppoMove();
    
    whatsAppMsj.value=`Hola ${myStore.whatsAppUser.name}, recuerda que hemos quedado en vernos `
    +` el día *${DateServices.changeFormatToDate(myStore.whatsAppUser.newAppoDate)} a las ${DateServices.removeSeconds(myStore.whatsAppUser.newAppoStart)}*. Un saludo!`;
    sendReminder(changedDateArray.value[0]);
}

async function executeAppoChange(dataToSend){
    let response=await DataServices.updateAppo(JSON.stringify(dataToSend));
    changedDateArray.value=response.data;
    buildCalendarArray(dateFilter.value);

    cancelAppoMove();

    //Si devuelve el mismo id de cita es que se ha hecho el cambio, si no es que no se ha hecho
    //Ojo porque lo que viene es una lista de citas
    //Si todo ha ido bien se puede enviar el whatsapp   
    if (changedDateArray.value[0].id===dataToSend.id) sendReminder(changedDateArray.value[0]);
    whatsAppMsj.value=`Hola ${myStore.whatsAppUser.name}, recuerda que hemos cambiado la fecha de la cita que tenías (el ${DateServices.changeFormatToDate(myStore.whatsAppUser.oldAppoDate)} a las ${myStore.whatsAppUser.oldAppoStart}),`
    +` al día *${DateServices.changeFormatToDate(myStore.whatsAppUser.newAppoDate)} a las ${DateServices.removeSeconds(myStore.whatsAppUser.newAppoStart)}*. Un saludo!`;
}

function cancelAppoMove(){
    myStore.onChanging=false;
    // myStore.whatsAppUser={};
    document.querySelectorAll(`#div_calendar p`).forEach(eachP=> eachP.style.border=`1px solid black`);
}

function deleteAppoDone(value){
    if(value)buildCalendarArray(dateFilter.value);
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
        <span @click="sendWhatsApp(whatsAppMsj)">Avisar por whatsApp</span>
    </div>
    <div id="div_changingAppo" v-if="myStore.onChanging">
        <span @click="cancelAppoMove()">Cancelar Cambio de fecha</span>
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
            @contextmenu.prevent.stop="showContextMenu"            
            @pointerdown="changeWeekAppointment">
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
            <PopUpMenuComponent v-on:-appo-delete-done="deleteAppoDone"/>
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
    overflow: hidden;
    
    color: var(--color-text);
    height: 5vh;   
    align-items: center;
    display:grid;
    border-radius: 2px;
    
}

p:hover{
    overflow: visible;
}

p:hover span{
    background-color: var(--color-background);
    align-self: center;
}

#div_calendar p:not(:empty){
    cursor: grab;
}

#div_calendar p:nth-of-type(9n+1), #div_calendar p:nth-of-type(9n+2), #div_calendar p:nth-of-type(-n+9){
    cursor:default;
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

#div_calendar p:nth-child(-n+9){
    height: max-content;
}

@media screen and (max-width: 430px) {
    *{
        font-size: small !important;
    }
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

    #div_daysInWeek p{
        font-size: smaller;
        margin-bottom: 0;
        line-height: unset;
        padding: 0;
    }  

    #div_calendar p{
        height: 4vh;
    }

    #div_calendar p span{
        font-size: x-small !important;
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

#div_changingAppo{
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

.highlight{
    box-shadow: 1px 1px 1px var(--color-text);
    background-color: var(--color-background-text2);
}



</style>