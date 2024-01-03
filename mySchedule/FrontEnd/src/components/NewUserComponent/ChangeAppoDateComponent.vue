<script setup>
import { ref, watch } from 'vue'
import { myUserStore} from '../../services/PiniaServices';
import DataServices from '../../services/DataServices';
import PasswordPopUp from './PasswordPopUp.vue';

const myStore=myUserStore();
const chbxState=ref(false);

function newUserAppo(){
    document.getElementById("div_darkness").classList.remove("invisible");

    let allInputs=document.querySelectorAll("#fieldset_UserData input");
    let onlyRequiredInputs=Array.from(allInputs).filter((eachOne)=>{
        return eachOne.hasAttribute("required") && eachOne.value==="";
    });
    
    if (onlyRequiredInputs.length!==0){
        myStore.msgToUser="No se ha realizado ninguna operación\n"
        +"Por favor, completa:";
        for(let eachOne in onlyRequiredInputs){
            myStore.msgToUser+="\n- "+onlyRequiredInputs[eachOne].title;
            onlyRequiredInputs[eachOne].style.backgroundColor="var(--color-background-text2)";
        }

        document.getElementById("aside_feedback").classList.remove("invisible");
    }
    //revisa con una expresion regular el email
    else if(!/\w+@\w+\.+\w{2,}/.test(document.querySelector("#input_userEmail").value)){
        myStore.msgToUser="No se ha realizado ninguna operación\n"
        +"Por favor, revisa el email introducido:";
        document.getElementById("aside_feedback").classList.remove("invisible");
        document.querySelector("#input_userEmail").style.backgroundColor="var(--color-background-text2)";
    }
    else{  
        createNewPasswordDialog();
    }
}

watch(()=>myStore.user.password, (thePassword)=>{
    if(thePassword.trim()!==''){
        createNewUser();
    }
    
})

async function createNewUser(){

    let dataToSend={
        name: myStore.user.name,
        alias:myStore.user.alias,
        nif: myStore.user.nif,
        surname1: myStore.user.surname1,
        surname2: myStore.user.surname2,
        email: myStore.user.email,
        phone:myStore.user.phone,
        notes:myStore.user.notes,
        password:myStore.user.password,
        price:myStore.user.price,
    }

    let response= await DataServices.saveNewUser(dataToSend);
    console.log(response.data);
    //Mostrar feedback a usuario (borrar inputs?)
    //Si viene bien, volcar a whatsappuser y elegir fecha
    //crear nueva fecha

}

function createNewPasswordDialog(){
    document.getElementById("aside_newPassDialog").classList.remove("invisible");
}

</script>

<template>
    <div>
        <button @click="newUserAppo()">Crear usuario</button>        
        <input type="checkbox" id="chbx_withoutAppo" v-model="chbxState">
        <label for="chbx_withoutAppo"> No asignar fecha de cita</label>
    </div>
    <PasswordPopUp/>
    
    
</template>

<style scoped>



</style>