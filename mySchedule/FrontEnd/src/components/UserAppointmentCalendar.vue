<script setup>
import { ref, watch } from 'vue';
import { myUserStore } from '../services/PiniaServices';
import {DefineCalendarBasics} from '../services/GraphCalendarServices'

const myMonthlyArray=ref([]);
const myStore=myUserStore();

let currentDate=new Date(Date.now());

//Construye el array que se mostrará en el calendario
function BuildCalendarArray(currentDate){
    let sessionMinutes=90;
    let timeWindows=["08:00", "21:00"];
    myMonthlyArray.value=DefineCalendarBasics(sessionMinutes, timeWindows, null);

    myStore.AllUsers.forEach((eachUser)=>{
        if((new Date(eachUser.nextAppoDate).getMonth()===currentDate.getMonth()) &&
        (new Date(eachUser.nextAppoDate).getFullYear()===currentDate.getFullYear())){
            console.log(("hola"));
            
        }
        else{
            console.log("no");
        }
        
    })

}

watch(()=> myStore.AllUsers, ()=>{
    console.log(currentDate.getMonth());
    BuildCalendarArray(currentDate);
});

const dateNow= new Date(Date.now());
const dateFilter = ref(dateNow.getFullYear() + "-" 
+ (dateNow.getMonth() + 1).toString().padStart(2,'0') + "-" 
+ dateNow.getDate().toString().padStart(2,'0'));

function resetDate(){
  dateFilter.value=dateNow.getFullYear() + "-" 
+ (dateNow.getMonth() + 1).toString().padStart(2,'0') + "-" 
+ dateNow.getDate().toString().padStart(2,'0');
}

</script>

<template>
    <section>
        <div id="div_searchBox">
        <input class="searchBox" type="date"  v-model="dateFilter"/>
        <span class="resetX" @click="resetDate()">x</span>
      </div>
        <div>
            <p v-for="(item,index) in myMonthlyArray" :key="item">{{ myMonthlyArray[index].tag }}</p>                      
        </div>
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

</style>