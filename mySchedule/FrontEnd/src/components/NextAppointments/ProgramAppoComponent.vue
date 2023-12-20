<script setup>
import {useRouter} from 'vue-router'

const props=defineProps({
    userInfo:Object,
})

const router=useRouter();

function newAppoDate(id){
    console.log(id);
    router.push("/newAppointment");

}

</script>

<template>
    <input class="AppoButton" type="button" :value="'Programar cita para '+props.userInfo.name" @click="newAppoDate(props.userInfo.id)">
</template>

<style scoped>
.AppoButton{
    background-color: #95BABD;
    border-radius: 5px;
    font-weight: bold;
    margin-left: 1vw;
}

</style>