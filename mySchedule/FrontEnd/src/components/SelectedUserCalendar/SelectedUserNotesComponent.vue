<script setup>
import {myUserStore} from '../../services/PiniaServices';
const myStore=myUserStore();

</script>

<template>
    <fieldset id="fieldset_userNotes">
        <legend>Notas generales</legend>
        <textarea v-model="myStore.user.notes" readonly>
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
    /* pointer-events: none; */
}

</style>