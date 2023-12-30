<script setup>
import { myUserStore } from '../../services/PiniaServices';
import { ref, watchEffect, onUpdated, watch } from 'vue'
import DateServices from '../../services/DateServices';

const myStore=myUserStore();
const myNote=ref();

const props=defineProps({
    incommingValue: {
        type: Number,
        default: -1,
    },
})

watchEffect(() => {
    if (props.incommingValue !== undefined && props.incommingValue!==-1) {
        myNote.value = myStore.user.appointmentsList.find(
            (eachAppo) => eachAppo.id === props.incommingValue
        );

        if (myNote.value) {
            myStore.appo.id = myNote.value.id;
            myStore.appo.appoDate = myNote.value.appoDate;
            myStore.appo.appoStart = myNote.value.appoStart;
            myStore.appo.notes = myNote.value.notes;
            myStore.appo.userID.id = myStore.user.id;
        }
    }
}),

watchEffect(()=> myStore.user.id, () => {
    myNote.value=null;
    myStore.appo.id=null;
    myStore.appo.appoDate=null;
    myStore.appo.appoStart=null;
    myStore.appo.notes=null;
    myStore.appo.userID.id=null;
}),

watch(()=> myStore.appo.id, () =>{
    myStore.onChanging=false;
    myStore.appo.notesAdd="";
}),

onUpdated(() => {
    if(document.querySelectorAll("#fieldset_appoNotes textarea").length===2){
        document.querySelector("#fieldset_appoNotes textarea:nth-of-type(2)").focus();
        //Hace scroll hasta el final del textArea donde estan las notas ya escritas
        document.querySelector("#fieldset_appoNotes textarea:nth-of-type(1)").scrollTop=document.querySelector("#fieldset_appoNotes textarea:nth-of-type(1)").scrollHeight;;
    }        
})

</script>

<template>
<fieldset id="fieldset_appoNotes">
    <legend v-if="myStore.appo.appoDate">Notas de la sesion del {{ DateServices.changeFormatToDate(myStore.appo.appoDate) }}
    a las {{ DateServices.removeSeconds(myStore.appo.appoStart) }}</legend>
    <textarea v-if="myStore.appo.appoDate"
            v-model="myStore.appo.notes"
            @focus="focusToChange" readonly>
    </textarea>
    <textarea class="txtArea_newNotes" type="text" v-if="myStore.onChanging" v-model="myStore.appo.notesAdd" placeholder="Añade nuevas notas aqui...">
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
    max-height: 100%;
    box-sizing: border-box;
    border:none;
    padding-left: 1vw;
    border-radius: 5px;
}
.txtArea_newNotes{
    height: 50%;
}

</style>