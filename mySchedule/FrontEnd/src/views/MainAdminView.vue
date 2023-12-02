<script setup>
import UserNavComponent from '../components/UserNavComponent.vue';
import axiosConnection from "../services/DataServices.js";
import { myUserStore } from "../services/PiniaServices";
import { onBeforeMount } from 'vue'

const myStore = myUserStore();


//Recoge el DTO de los datos abreviados de usuario y los guarda en Pinia
//asi estarán disponibles para todos los hijos
onBeforeMount(async() => {
    myStore.AllUsers = (await axiosConnection.getAllUsers()).data;      
})

</script>
<template>
    <UserNavComponent/>
</template>

<style scoped>
template{
    display: flex;
    flex-direction: row;
    justify-content: space-around;
}
</style>