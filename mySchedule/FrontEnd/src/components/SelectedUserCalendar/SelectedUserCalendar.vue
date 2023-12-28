<script setup>
import UserDataComponent from './UserDataComponent.vue';
import { ref, watch } from 'vue'
import { myUserStore } from '../../services/PiniaServices';
import AppoListComponent from './AppoListComponent.vue';
import SelectedAppoNotesComponent from './SelectedAppoNotesComponent.vue'; 
import SelectedUserNotesComponent from './SelectedUserNotesComponent.vue';
import ButtonMenuComponent from './ButtonMenuComponent.vue';

// const myEmits = defineEmits(['selected-appo-emit']);

const myUserData=ref({});
const myStore=myUserStore();
const mySelectedAppo=ref();

myUserData.value=myStore.user;

watch(()=>myStore.user.name, ()=>{
    myUserData.value=myStore.user;
})

</script>

<template>
    <section id="section_mainSelectedUserInfo">
        <div id="div_AppoDates">
            <AppoListComponent @selected-appo-emit="(incommingValue)=> mySelectedAppo=incommingValue"/>
        </div>

        <div id="div_UserData">
            <UserDataComponent :userData="myUserData" />
        </div>

        <div id="div_UserNotes">
            <SelectedUserNotesComponent/>
        </div>

        <div id="div_AppoNotes">
            <SelectedAppoNotesComponent :incommingValue="mySelectedAppo"/>
        </div>

        <div id="div_options">
            <ButtonMenuComponent/>
        </div>
    </section>
</template>

<style scoped lang="scss">

#section_mainSelectedUserInfo{
    display:grid;
    grid-template-columns: 1fr 3fr;
    grid-template-rows: 1fr 1fr 1.5fr 0.5fr;
    width: 80vw;
    max-height: 70vh;
    gap: 1vh;
    

    div{      
        border-radius: 5px;
    }

    #div_AppoDates{
        grid-row: 1 / span 2;
        align-self: flex-start;
        height: 100%;        
    }

    #div_AppoNotes{
        grid-column: 1 / span 2;
    }

    #div_options{
        grid-column: 1 / span 2;
    }
}
</style>