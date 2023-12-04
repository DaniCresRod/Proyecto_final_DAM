<script setup>
import NextAppoComponent from '../components/AllAppointmentComponent.vue'
import { ref } from 'vue'

const wordFilter=ref();
const dateNow= new Date(Date.now());
const NoAppoFilter=ref(false);
const viewOrNot=ref("ver");

const dateFilter = ref(dateNow.getFullYear() + "-" 
+ (dateNow.getMonth() + 1).toString().padStart(2,'0') + "-" 
+ dateNow.getDate().toString().padStart(2,'0'));

function resetDate(){
  dateFilter.value=dateNow.getFullYear() + "-" 
+ (dateNow.getMonth() + 1).toString().padStart(2,'0') + "-" 
+ dateNow.getDate().toString().padStart(2,'0');
}

function resetName(){
  wordFilter.value="";
}

const nullAppoCustomers = ref('');

function handleNullAppoCustomers(value) {
  nullAppoCustomers.value = value; 
}

function viewNoAppo(){
  NoAppoFilter.value=!NoAppoFilter.value;
  if(viewOrNot.value==="ver") viewOrNot.value="X";
  else viewOrNot.value="ver";
}

</script>

<template>
  <main>
    <section>
      <div v-if="nullAppoCustomers>0">
        <span class="NoAppoCust" >Hay {{ nullAppoCustomers }} pacientes sin cita programada </span>
        <input class="NoAppoCust viewButton" type="Button" :value="viewOrNot" @click="viewNoAppo()"/>
      </div>
      <div>
        <input class="searchBox" type="date"  v-model="dateFilter"/>
        <span class="resetX" @click="resetDate()">x</span>
      </div>
      <div>
        <input class="searchBox" type="text" placeholder="Buscar paciente..." v-model="wordFilter"/>
        <span class="resetX" @click="resetName()">x</span>
      </div>      
    </section>

    <NextAppoComponent :myfilter="wordFilter" :myfilterdate="dateFilter" :myfilterNoAppos="NoAppoFilter" @nullappocustomers="handleNullAppoCustomers"/>
  </main>
</template>

<style scoped>
section{
  margin: 0 auto;
  padding: 2vh 2vw;
  display: flex;
  justify-content: flex-end;
  align-items: baseline;
  flex-wrap: wrap;
}

div{
  display: flex;
  justify-content: flex-end;
  align-items: baseline;
  margin-top: 1vh;
}

.NoAppoCust{
  font-size: smaller;
  font-weight: bold;
}

.viewButton{
  border-radius: 15px;
  margin-left: 1vw;
  margin-right: 1em;
}
</style>
