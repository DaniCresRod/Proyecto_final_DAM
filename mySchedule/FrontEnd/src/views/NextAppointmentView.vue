<script setup>
import NextAppoComponent from '../components/NextAppointmentComponent.vue'
import { ref, onBeforeMount} from 'vue'

const wordFilter=ref();
const dateNow= new Date(Date.now());

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

onBeforeMount(() => {
  console.log("hola");
})

const nullAppoCustomers = ref('');

function handleNullAppoCustomers(value) {
  nullAppoCustomers.value = value;
  console.log(nullAppoCustomers.value);
}


// watch(props, ()=>{
//   console.log(props.nullappocustomers);
// })

</script>

<template>
  <main>
    <div>
      <span v-if="nullAppoCustomers !== ''">Hay {{ nullAppoCustomers }} pacientes sin cita programada</span>
      <input class="searchBox" type="date"  v-model="dateFilter"/>
      <span class="resetX" @click="resetDate()">x</span>
      <input class="searchBox" type="text" placeholder="Buscar paciente..." v-model="wordFilter"/>
      <span class="resetX" @click="resetName()">x</span>
      
    </div>

    <NextAppoComponent :myfilter="wordFilter" :myfilterdate="dateFilter" @nullappocustomers="handleNullAppoCustomers"/>
  </main>
</template>

<style scoped>
div{
  margin: 0 auto;
  padding: 2vh 2vw;
  display: flex;
  justify-content: flex-end;
  align-items: baseline;
}

.searchBox{
  border-radius: 10px;
  padding: 0.5vh 1.5vw ;
  margin-left: 1.5vw;
}

.resetX{
  font-weight: bolder;
  margin-left: 0.5vw;
  border:1px solid transparent;
}

.resetX:hover{
  background-color: white;
  border-radius: 15px;
  border:1px solid black; 
  line-height: 1vh;
  background-size: 3%;
  cursor: pointer;
  
}
</style>
