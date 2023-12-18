<script setup>
import { myUserStore } from '../services/PiniaServices';
import DataServices from '../services/DataServices';

let myStore=myUserStore();

function changingAppoMode(){
    try{
        myStore.onChanging=true;
    
        document.getElementById("div_contextMenu").classList.add("invisible");
        document.getElementById("div_changingAppo").classList.remove("invisible");
    }
    catch{
        myStore.onChanging=false;
        document.getElementById("div_changingAppo").classList.add("invisible");
        console.log("error");
    }    
}

async function deleteAppo(){
    try{
        let response= await DataServices.deleteAppoById(myStore.whatsAppUser.appoId);
        console.log(response);
    }
    catch{
        console.log("error al borrar");
    }
}

</script>

<template>
    <div>
        <ul>
            <li @click="changingAppoMode">Mover la cita</li>
            <li @click="deleteAppo">Borrar la cita</li>
        </ul>
        
    </div>
</template>

<style scoped>

ul{
    list-style: none;
    width: fit-content;
    text-wrap: nowrap;
    padding: 0;
    background-color: var(--color-background-text);
}
li{
    border:1px solid var(--color-border);
    padding: 0.5vh 0.5vw;
    cursor: pointer;
}

</style>