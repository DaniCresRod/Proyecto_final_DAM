<script setup>
import { ref, watch } from 'vue';
import { myUserStore} from '../../services/PiniaServices';
import DataServices from '../../services/DataServices';
import PasswordPopUp from './PasswordPopUp.vue';
import { OpenFeedbackDialog } from '../../services/UserFeedbackService';
import router from '../../router';

const myStore=myUserStore();
const chbxState=ref(true);

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
        OpenFeedbackDialog();
    }
    //revisa con una expresion regular el email
    else if(!/\w+@\w+\.+\w{2,}/.test(document.querySelector("#input_userEmail").value)){
        myStore.msgToUser="No se ha realizado ninguna operación\n"
        +"Por favor, revisa el email introducido:";
        OpenFeedbackDialog();
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

    let response = await DataServices.saveNewUser(dataToSend);
    console.log(response.data);

    if(response.data.newUser){
        if(document.getElementById("chbx_withoutAppo").value){
            myStore.onChanging=true;

            myStore.whatsAppUser.userId=response.data.id;
            myStore.whatsAppUser.name=response.data.name;
            myStore.whatsAppUser.phone=response.data.phone;
            router.push("/AdminView");
        }
    }
    else{
        myStore.msgToUser=response.data.feedback;
        myStore.user.password='';
        OpenFeedbackDialog();
    }
    //Mostrar feedback a usuario (borrar inputs?)
    //Si viene bien, volcar a whatsappuser y elegir fecha
    //crear nueva fecha

}

function createNewPasswordDialog(){
    document.getElementById("aside_newPassDialog").classList.remove("invisible");
}

</script>

<template>
    <fieldset>
        <button @click="newUserAppo()">Crear usuario</button>        
        <div>
            <input type="checkbox" id="chbx_withoutAppo" v-model="chbxState">
            <label for="chbx_withoutAppo"> Asignar fecha de cita</label>
        </div>        
    </fieldset>
    <PasswordPopUp/>    
</template>

<style scoped>
fieldset{
    display: flex;
    flex-direction: row;
    align-items:center;
    gap: 1vw;
}

div{
    display: flex;
    flex-direction: row;
    align-items:center;
    gap: 0.5vw;
}
</style>