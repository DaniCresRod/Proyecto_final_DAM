<script setup>
import { myUserStore } from '../../services/PiniaServices';
import { ref, watch } from 'vue'

const myStore=myUserStore();
const myNote=ref();

const props=defineProps({
    incommingValue: {
        type: Number,
        default: 0,
    },
})

//pasar a pinia.appo para hacerla de doble diren por si hay que editar
watch( () => props.incommingValue, ()=>{
    myNote.value=myStore.user.appointmentsList.find(
        (eachAppo)=>eachAppo.id === props.incommingValue).notes;
}),

watch(()=> myStore.user.id, () => myNote.value=null)



</script>

<template>hola {{ props.incommingValue }}

<fieldset v-if="props.incommingValue">
    <textarea 
            v-model="myNote">
                
            </textarea>

</fieldset>
    <!-- <textarea cols="30" rows="10" 
            v-model="myStore.user.appointmentsList.find(
                    (eachAppo)=>eachAppo.id === props.incommingValue).notes">
                
            </textarea> -->
</template>

<style scoped>

</style>