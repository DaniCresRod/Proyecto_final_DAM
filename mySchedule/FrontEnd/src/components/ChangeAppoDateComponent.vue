<script setup>
import { myUserStore } from '../services/PiniaServices';
import DataServices from '../services/DataServices';

const myStore=myUserStore();

function newUserAppo(){
    if(myStore.user.name==="" || myStore.user.nif==="" 
    || myStore.user.phone==="" || myStore.user.email===""){

    }

}


</script>

<template>
    <div>
        <button @click="newUserAppo()">Crear usuario</button>        
        <input type="checkbox" id="chbx_withoutAppo">
        <label for="chbx_withoutAppo"> No asignar fecha de cita</label>
    </div>
</template>

<style scoped>

</style>