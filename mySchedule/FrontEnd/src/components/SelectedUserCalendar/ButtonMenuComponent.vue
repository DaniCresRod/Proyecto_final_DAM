<script setup>
import { myUserStore } from '../../services/PiniaServices';
import { watchEffect } from 'vue'
import DataServices from '../../services/DataServices';

const myStore=myUserStore();

function cancelChanges(){
    myStore.onChanging=false;
    myStore.appo.notesAdd="";
    document.querySelector("#fieldset_appoNotes textarea").classList.remove("txtArea_newNotes");
}

function modifyAppo(){
    myStore.onChanging=true;
    document.querySelector("#fieldset_appoNotes textarea").classList.add("txtArea_newNotes");
}

async function upgradeAppoNotes(){
    let dataToSend={
        id: myStore.appo.id,
        appoDate: myStore.appo.appoDate,
        appoStart: myStore.appo.appoStart,
        notes: myStore.appo.notesAdd,
        userID:{
            id: myStore.appo.userID.id}
    }

    let response=await DataServices.updateAppoNotes(JSON.stringify(dataToSend));

    if(response.data!==null){
        cancelChanges();
        myStore.appo.notes=response.data.notes;
    }
    else{
        window.alert("Se ha producido un error y no se han cargado los datos");
    }
}

//Aqui si nos interesa que se puedan modificar las notas del usuario.
function changeUserData(){
    let buttonFunction=document.getElementById("button_changeUserData");
    
    console.log(buttonFunction.title);

    document.querySelector("#fieldset_userNotes textarea").removeAttribute("readonly");
    document.querySelectorAll("#fieldset_UserData section").forEach((eachSection)=> eachSection.style.pointerEvents="all");


}

watchEffect(()=>myStore.onChanging, )

</script>

<template>
    <section>
        <div>
        <button v-if="!myStore.onChanging && myStore.appo.id!==null"  @click="modifyAppo" title="Modificar Notas de Sesión">Modificar</button>
        <button v-if="myStore.onChanging" @click="upgradeAppoNotes" title="Guardar Cambios">Guardar Cambios</button>
        <button v-if="myStore.onChanging" @click="cancelChanges" title="Cancelar Cambios">Cancelar Cambios</button>
        <button>Nueva cita</button>        
    </div>
    <div>
        <button id="button_changeUserData" @click="changeUserData" title="Editar">Editar Datos Personales</button>
    </div>
    </section>
    
</template>

<style scoped>
section{
    display: flex;
    justify-content:space-between;
}

div{
    display: flex;
    flex-direction: row;
    column-gap: 1.5vw;
}

button{
    background-color: #fff;
    color: var(--color-text);
    font-weight: bold;
    border-color: var(--color-text);
}

</style>