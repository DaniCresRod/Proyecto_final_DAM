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
        }

        document.getElementById("aside_feedback").classList.remove("invisible");
    }
    else{
        if(chbxState.value===false){
            console.log("hola");
        }
        else{
            console.log("no");
        }
        //Expresiones regulares para chequear los inputs
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