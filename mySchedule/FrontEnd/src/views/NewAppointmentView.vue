<script setup>
import UserDataComponent from '../components/SelectedUserCalendar/UserDataComponent.vue';
import ChangeAppoDateComponent from '../components/NewUserComponent/ChangeAppoDateComponent.vue';
import SelectedUserNotesComponent from '../components/SelectedUserCalendar/SelectedUserNotesComponent.vue';
import { onMounted, onUnmounted } from 'vue'
import { myUserStore } from '../services/PiniaServices';

onMounted(()=>{
  document.querySelectorAll("#fieldset_UserData section").forEach((eachSection)=> eachSection.style.pointerEvents="all");
  document.querySelectorAll("#fieldset_UserData input").forEach((eachSection)=> eachSection.setAttribute("autocomplete", "off"));
  document.querySelectorAll("#fieldset_userNotes textarea").forEach((eachSection)=> eachSection.removeAttribute("readonly"));
  myUserStore().user ={
            id:'',
            nif:'',
            name:'',
            surname1:'',
            surname2:'',
            alias:'',
            email:'',
            phone:'',
            password:'',
            notes:'',
            price:60,
            appointmentsList:[]
        };
}), 

onUnmounted(()=>{
  document.querySelectorAll("#fieldset_UserData section").forEach((eachSection)=> eachSection.style.pointerEvents="none");
  document.querySelectorAll("#fieldset_UserData input").forEach((eachSection)=> eachSection.setAttribute("autocomplete", "on"));
  document.querySelectorAll("#fieldset_userNotes textarea").forEach((eachSection)=> eachSection.setAttribute("readonly",''));
  myUserStore().user={id:''};
})


</script>

<template>
  <div class="div_newUser">
    <UserDataComponent :userData="myUserStore().user" />
    <SelectedUserNotesComponent/>
    <ChangeAppoDateComponent />
  </div>
</template>

<style scoped>

.div_newUser{
  display: flex;
  flex-direction: column;
  gap:1vh;
}

/* @media (min-width: 1024px) {
  .about {
    min-height: 75vh;
    display: flex;
    align-items: center;
  }
} */
</style>
