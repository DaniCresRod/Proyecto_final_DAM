<script setup>
import { ref } from 'vue'
import { myUserStore} from '../services/PiniaServices';

const myStore=myUserStore();
const chbxState=ref(false);

function newUserAppo(){

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
        if(chbxState.value===false){
            console.log("hola");
        }
        else{
            console.log("no");
        }
        
        //llamar a axios
    }
}


</script>

<template>
    <div>
        <button @click="newUserAppo()">Crear usuario</button>        
        <input type="checkbox" id="chbx_withoutAppo" v-model="chbxState">
        <label for="chbx_withoutAppo"> No asignar fecha de cita</label>
    </div>
</template>

<style scoped>

</style>