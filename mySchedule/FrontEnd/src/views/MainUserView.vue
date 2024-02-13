<script setup>
import DataServices from '../services/DataServices';
import DateServices from '../services/DateServices';
import { ref, onBeforeMount } from 'vue'
import { myUserStore } from '../services/PiniaServices';

const nextAppo=ref();
const nextAppos=ref([]);

async function userNextAppo(){
    let response = await DataServices.getUserById(window.localStorage.getItem("userId"));
    myUserStore().user=response.data;    
    
    if(response.data.appointmentsList.length >0){

        nextAppos.value=response.data.appointmentsList.filter(eachDate=>{
            return (new Date(eachDate.appoDate+" "+eachDate.appoStart) > Date.now());            
        });
        if(nextAppos.value.length==0){
            nextAppo.value="No tienes una cita concertada"
        }
        else{
            nextAppo.value=nextAppos.value.pop();
        }        
    }
}
onBeforeMount(() => {
    userNextAppo();
})

</script>

<template>
    <div class="logInForm">        
        <p class="p_title">Mi pr&oacute;xima cita:</p>
        <span>{{ DateServices.changeFormatToDate(nextAppo.appoDate)}}</span>
        <p>({{ DateServices.getDayFromDate(nextAppo.appoDate) }}), 
        a las {{ DateServices.removeSeconds(nextAppo.appoStart) }}h.</p> 
    </div>

    <div class="moreAppos" v-if="nextAppos.length > 0">
        <p>Tus otras citas: </p>
        <ul>
            <li v-for="appo of nextAppos" :key="appo">
                <p>{{ DateServices.changeFormatToDate(appo.appoDate)}}
                 ({{ DateServices.getDayFromDate(appo.appoDate) }}), 
                a las {{ DateServices.removeSeconds(appo.appoStart) }}h.</p> 
            </li>
        </ul>
    </div>


</template>

<style scoped>
div{
    margin-top: 5vh;
    text-align: center;
}
span{
    font-weight: bolder;
    font-size: xx-large;
    
    color: var(--color-text);
}

.moreAppos{
    margin: 5vh auto;
    width: fit-content;
    text-wrap: wrap;
    text-align: left;
}

.p_title{
    color: var(--color-text2);
}
</style>