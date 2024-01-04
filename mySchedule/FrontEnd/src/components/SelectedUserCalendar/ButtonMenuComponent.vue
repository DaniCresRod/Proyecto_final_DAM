<script setup>
import { myUserStore } from '../../services/PiniaServices';
import DataServices from '../../services/DataServices';
import { OpenFeedbackDialog } from '../../services/UserFeedbackService';
import areYouSure from '../AreYouSureComponent.vue'

const myStore=myUserStore();
let originalData;

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
    if(myStore.appo.notesAdd.trim()!==""){
        
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
            console.log(response.data);
            myStore.msgToUser="Se guardó correctamente";
        }
        else{
            window.alert("Se ha producido un error y no se han cargado los datos");
        }
    }
    else{
        cancelChanges();
        myStore.msgToUser="No se han detectado cambios y no se realizará ninguna operación";
    }
    OpenFeedbackDialog();
}

//Aqui si nos interesa que se puedan modificar las notas del usuario.
async function changeUserData(){
    let buttonFunction=document.getElementById("button_changeUserData");    
    
    if(buttonFunction.title==="Editar"){
        document.querySelector("#fieldset_userNotes textarea").removeAttribute("readonly");
        document.querySelectorAll("#fieldset_UserData section").forEach((eachSection)=> eachSection.style.pointerEvents="all");

        buttonFunction.setAttribute("title", "Guardar");
        buttonFunction.innerText="Guardar Datos Personales";

        originalData={
            nif: myStore.user.nif,
            name: myStore.user.name,
            surname1: myStore.user.surname1,
            surname2: myStore.user.surname2,
            alias: myStore.user.alias,
            email: myStore.user.email,
            phone: myStore.user.phone,
            notes: myStore.user.notes,
            price: myStore.user.price
        }
    }
    else{
        document.querySelector("#fieldset_userNotes textarea").setAttribute("readonly", "");
        document.querySelectorAll("#fieldset_UserData section").forEach((eachSection)=> eachSection.style.pointerEvents="none");

        buttonFunction.setAttribute("title", "Editar");
        buttonFunction.innerText="Editar Datos Personales";

        let dataToSend={
            nif: myStore.user.nif,
            name: myStore.user.name,
            surname1: myStore.user.surname1,
            surname2: myStore.user.surname2,
            alias: myStore.user.alias,
            email: myStore.user.email,
            phone: myStore.user.phone,
            notes: myStore.user.notes,
            price: myStore.user.price
        }

        //Solo actualiza si ha cambiado algo
        if(JSON.stringify(originalData)!==JSON.stringify(dataToSend)){
            let response=await DataServices.updateUser(myStore.user.id, JSON.stringify(dataToSend));
            myStore.msgToUser=response.data;
        }
        else{
            myStore.msgToUser="No se han detectado cambios y no se realizará ninguna operación";
        }
        OpenFeedbackDialog();   
    }
}

async function deleteUser(){
    document.getElementById("aside_rUSure").classList.remove("invisible");
    document.getElementById("div_darkness").classList.remove("invisible");  
}

async function dareToDelete(valor){
    document.getElementById("div_darkness").classList.add("invisible");
    document.getElementById("aside_rUSure").classList.add("invisible");
    if(valor){
        let response=await DataServices.deleteById(myStore.user.id);
        myStore.msgToUser=response.data;

        OpenFeedbackDialog();

        let userToDelete=myStore.AllUsers.find(user=>user.id===myStore.user.id);
        let myDeletedUser=myStore.AllUsers.indexOf(userToDelete);
        myStore.AllUsers.splice(myDeletedUser, 1);

        myStore.user.id='';        
        // myStore.AllUsers = (await DataServices.getAllUsers()).data;
        
        
    }    
}

</script>

<template>
    <section>
        <div>
        <button v-if="!myStore.onChanging && myStore.appo.id!==null"  @click="modifyAppo" title="Modificar Notas de Sesión">Modificar</button>
        <button v-if="myStore.onChanging" @click="cancelChanges" title="Cancelar Cambios">Cancelar Cambios</button>
        <button v-if="myStore.onChanging" @click="upgradeAppoNotes" title="Guardar Cambios">Guardar Cambios</button>
        <button>Nueva cita</button>        
    </div>
    <div>
        <button id="button_changeUserData" @click="changeUserData" title="Editar">Editar Datos Personales</button>
        <button id="button_changeUserData" @click="deleteUser" title="Borrar Usuario">Borrar Usuario</button>
    </div>
    </section>
    <areYouSure @sure-to-delete="dareToDelete"/>
</template>

<style scoped>
section{
    display: flex;
    justify-content:space-between;
    column-gap: 1.5vw;
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