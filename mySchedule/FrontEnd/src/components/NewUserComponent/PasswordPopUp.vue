<script setup>
import InputServices from '../../services/InputServices';
import { onMounted, onBeforeUnmount} from 'vue';
import { myUserStore } from '../../services/PiniaServices';

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

function setPassword(okOrCancel){
    if(okOrCancel){
        myUserStore().user.password=document.getElementById('input_password_1').value;
        document.getElementById("aside_newPassDialog").classList.add("invisible");
        document.getElementById("div_darkness").classList.add("invisible");     
    }
    else{
        document.getElementById("aside_newPassDialog").classList.add("invisible");
        document.getElementById("div_darkness").classList.add("invisible");
                
    }
    //Borrar los campos
    document.querySelectorAll("#aside_newPassDialog input").forEach((eachInput)=>{
        eachInput.value='';
    });
    checkPasswords();
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

            <div>
                <button disabled @click="setPassword(true)">Asignar contraseña</button>
                <button @click="setPassword(false)">Cancelar</button>
            </div>
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

.aside_newPassPosition fieldset{
    margin-bottom: 0;
}

fieldset div{
    margin-bottom: 0.5vh;
}

fieldset div:nth-last-of-type(2){
    margin-bottom: 2vh;
}

fieldset div:last-of-type{
    display: flex;
    justify-content: space-between;
    gap: 2vh;
    margin-bottom: 0;
}


</style>