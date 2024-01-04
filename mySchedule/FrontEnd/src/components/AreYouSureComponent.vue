<script setup>
import { onMounted, onBeforeUnmount} from 'vue';
import { myUserStore } from '../services/PiniaServices';

let observer;

function centerDialog(){
    let myDialog=document.getElementById("aside_rUSure");
    let dialogWidth=myDialog.clientWidth;
    let dialogHeight=myDialog.clientHeight;

    myDialog.style.top = (visualViewport.height / 3)-(dialogHeight /2)+"px";
    myDialog.style.left = (visualViewport.width / 2)-(dialogWidth /2)+"px";
}

onMounted(() => {
    observer=new MutationObserver((mutations) => {
        mutations.forEach((mutation)=>{
            if(mutation.attributeName === 'class' && !mutation.target.classList.contains('invisible')){
                centerDialog();
            }
        })
    });
    
    observer.observe(document.getElementById("aside_rUSure"), { attributes: true, attributeFilter: ['class'] });
    
    window.addEventListener('resize', centerDialog);
});

onBeforeUnmount(() => {
    window.removeEventListener('resize', centerDialog);
    observer.disconnect();       
});

</script>

<template>
    <aside id="aside_rUSure" class="invisible logInForm aside_newPassPosition">
        <fieldset>
            <p>¿Seguro que quieres eliminar el usuario {{ myUserStore().user.name }} {{ myUserStore().user.alias }}?</p>

            <div>
                <button @click="$emit('sureToDelete', true)">Si, seguro</button>
                <button @click="$emit('sureToDelete', false)">Cancelar</button>
            </div>
        </fieldset>        
    </aside>
</template>

<style scoped>

#aside_rUSure{
    max-width: 70vw;
}
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
    margin: 1vh 0 0.5vh;
    display: flex;
    justify-content: center;
    gap:2vw;
}
</style>