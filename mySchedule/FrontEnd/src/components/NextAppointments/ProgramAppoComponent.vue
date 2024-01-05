<script setup>
import { turnToSetUserAppo } from '../../services/SelectUserAppo';

const props=defineProps({
    userInfo:Object,
})



function newAppoDate(userInfo){
    console.log(userInfo);
    turnToSetUserAppo(userInfo)
}

</script>

<template>
    <input class="AppoButton" type="button" :value="'Programar cita para '+props.userInfo.name" @click="newAppoDate(props.userInfo)">
</template>

<style scoped>
.AppoButton{
    background-color: #95BABD;
    border-radius: 5px;
    font-weight: bold;
    margin-left: 1vw;
}
</style>