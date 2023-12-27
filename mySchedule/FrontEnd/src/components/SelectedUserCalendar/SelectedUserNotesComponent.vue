<script setup>
import {myUserStore} from '../../services/PiniaServices';
const myStore=myUserStore();

</script>

<template>
    <fieldset>
        <legend>Notas generales</legend>
        <textarea v-model="myStore.user.notes">
        </textarea>
    </fieldset>
</template>

<style scoped>
fieldset{
    width: 100%;
    height: 100%;
}


textarea{
    width: 100%;
    height: 100%;
    box-sizing: border-box;
    border:none;
    padding-left: 1vw;
    
}

</style>