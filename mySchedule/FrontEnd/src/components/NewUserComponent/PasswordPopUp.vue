<script setup>
import InputServices from '../../services/inputServices';
import { onMounted, onBeforeUnmount } from 'vue'
import { myUserStore } from '../../services/PiniaServices';
import DataServices from '../../services/DataServices';

let observer;

function centerDialog(){
    let myDialog=document.getElementById("aside_newPassDialog");
    let dialogWidth=myDialog.clientWidth;
    let dialogHeight=myDialog.clientHeight;

    myDialog.style.top = (visualViewport.height / 3)-(dialogHeight /2)+"px";
    myDialog.style.left = (visualViewport.width / 2)-(dialogWidth /2)+"px";
}

function checkPasswords(){
    if((document.getElementById("input_password_1").value===document.getElementById("input_password_2").value) 
    && document.getElementById("input_password_1").value!==''){
        document.querySelector("#aside_newPassDialog button").removeAttribute("disabled");
    }
    else{
        document.querySelector("#aside_newPassDialog button").setAttribute("disabled", '');
    }
}

function setPassword(){
    myUserStore().user.password=document.getElementById('input_password_1').value;
    document.getElementById("aside_newPassDialog").classList.add("invisible");
    //AXIOS
}

onMounted(() => {
    observer=new MutationObserver((mutations) => {
        mutations.forEach((mutation)=>{
            if(mutation.attributeName === 'class' && !mutation.target.classList.contains('invisible')){
                centerDialog();
            }
        })
    });
    
    observer.observe(document.getElementById("aside_newPassDialog"), { attributes: true, attributeFilter: ['class'] });
    
    window.addEventListener('resize', centerDialog);
});

onBeforeUnmount(() => {
    window.removeEventListener('resize', centerDialog);
    observer.disconnect();    
});

</script>

<template>
    <aside id="aside_newPassDialog" class="invisible logInForm aside_newPassPosition">
        <fieldset>
            <label for="input_password_1">Nueva contraseña:</label>
            <div class="passwordBound">
                <input id="input_password_1" type="password" required 
                @change="checkPasswords">
                <img id="imgModePassword_2" class="hideShowPass" src="@/assets/Images/iconoOculto.png" 
                alt="Mostrar u ocultar contraseña" 
                @click="InputServices.ToggleImg('input_password_1', 'imgModePassword_2')"/>
            </div>

            <label for="input_password_2">Confirmar nueva contraseña:</label>
            <div class="passwordBound">
                <input id="input_password_2" type="password"
                @input="checkPasswords">
                <img id="imgModePassword_3" class="hideShowPass" src="@/assets/Images/iconoOculto.png" 
                alt="Mostrar u ocultar contraseña" 
                @click="InputServices.ToggleImg('input_password_2', 'imgModePassword_3')"/>
            </div>

            <button disabled @click="setPassword">Asignar contraseña</button>

        </fieldset>        
    </aside>
</template>

<style scoped>

.aside_newPassPosition{
    position:absolute;
    z-index: 3;
    margin:0;
    background-color: white;
}

fieldset div:last-of-type{
    margin-bottom: 2vh;
}


</style>