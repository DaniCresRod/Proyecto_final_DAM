<script setup>
import { myUserStore } from '../../services/PiniaServices';
import { ref, watchEffect } from 'vue'
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
    if (props.incommingValue !== undefined) {
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
})

</script>

<template>
<fieldset>
    <legend v-if="myStore.appo.appoDate">Notas de la sesion del {{ DateServices.changeFormatToDate(myStore.appo.appoDate) }}
    a las {{ DateServices.removeSeconds(myStore.appo.appoStart) }}</legend>
    <textarea v-if="myStore.appo.appoDate"
            v-model="myStore.appo.notes"
            @focus="myStore.onChanging=true" readonly>
    </textarea>
    <input type="text" v-if="myStore.onChanging" v-model="myStore.appo.notesAdd" placeholder="Escribe aqui...">

</fieldset>
</template>

<style scoped>
fieldset{
    width: 100%;
    height: 100%;
}


textarea{
    width: 100%;
    height: 80%;
    box-sizing: border-box;
    border:none;
    padding-left: 1vw;
    border-radius: 5px;
}
input{
    width: 100%;
    height: 20%;
    box-sizing: border-box;
    border:1px solid var(--color-border);
    padding-left: 1vw;
    border-radius: 5px;

}

</style>