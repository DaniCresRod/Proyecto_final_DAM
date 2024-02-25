<script setup>
import mainView from './views/MainView.vue';
import { ref, watch, onBeforeMount } from 'vue'
import {LogIn, checkLogIn, closeDialog} from '../src/services/UserFeedbackService'
import { myUserStore } from './services/PiniaServices';
import InputServices from './services/InputServices';
import router from './router/index.js';

const isLogged=ref(false);
const isAdmin=ref(window.localStorage.getItem("userRol")==="Admin");
const logType=ref([]);

const userName=ref("");
const userPass=ref("");

watch(logType, async ()=>{  
  [isLogged.value, isAdmin.value] = await logType.value;
})

onBeforeMount(async () => {
  router.push('/');
  //Revisa si hay almacenado un valor valido en el token
  isLogged.value=await checkLogIn(); 
})

function logOut(){
  window.localStorage.setItem("userEmail", '');
  window.localStorage.setItem("userToken", '');
  window.localStorage.setItem("userId", '');
  window.localStorage.setItem("userName", '');
  window.localStorage.setItem("userRol", '');
  router.push('/');
}

</script>

<template>
    <header>
    <img alt="Brand Logo" class="logo" src="@/assets/Images/brainLogo.png"  />

    <div class="wrapper">

      <nav v-if="isAdmin && isLogged">
        <RouterLink to="/AdminView">Inicio</RouterLink>
        <RouterLink to="/newAppointment">Nuevo usuario</RouterLink>
        <RouterLink to="/allApointment">Próximas citas</RouterLink>
        <a href="" @click="logOut()">Salir</a>
      </nav>      
      <nav v-else-if="isLogged">
        <RouterLink to="/UserView">Mi Cita</RouterLink>
        <RouterLink to="/billView">Mis Facturas</RouterLink>
        <a href="" @click="logOut()">Salir</a>
      </nav>
      <nav v-else>
        
      </nav>

    </div>
  </header>

  <!-- En funcion del estado del login tendremos una pagina de inicio u otra -->
  <mainView v-if="isLogged"/>

  <form class="logInForm" v-else>
    <fieldset>
      <label for="logInEmail">Email <abbr title="Campo Requerido" aria-label="required">*</abbr></label>
      <input type="text" name="logInEmail" id="logInEmail" autocomplete="username" 
      v-model="userName"
      @keyup.enter="logType=LogIn(userName,userPass)">
    </fieldset>

    <fieldset>
      <label for="logInPassword">Contraseña <abbr title="Campo Requerido" aria-label="required">*</abbr></label>
      <div class="passwordBound">
        <input type="password" name="logInPassword" id="logInPassword" autocomplete="current-password" 
        v-model="userPass"
        @keyup.enter="logType=LogIn(userName,userPass)">
        <img id="imgModePassword_1" class="hideShowPass" src="@/assets/Images/iconoOculto.png" alt="Mostrar u ocultar contraseña" @click="InputServices.ToggleImg('logInPassword', 'imgModePassword_1')"/>
      </div>      
    </fieldset>
      
    <p class="invisible logInError" id="logInError"></p>

    <input id="btnLogIn" type="button" value="Log In" @click="logType=LogIn(userName,userPass)">    
    
  </form>

  <footer>
    <p>Daniel Crespo Rodr&iacute;guez</p>
    <p>Proyecto Desarrollo de Aplicaciones Multiplataforma</p>

  </footer>

  <aside id="aside_feedback" class="invisible">
    <article>{{ myUserStore().msgToUser }}</article>
    <div>
      <button @click="closeDialog">OK</button>
    </div>
  </aside>

  <div id="div_darkness" class="invisible darkness">

  </div>


  
</template>

<style>
.invisible{
  display:none !important;
  width: fit-content;
}

.logInError{
  color:red;
  align-self: center;
}

#btnLogIn{
  height: 6vh;
  min-height: 25px;
  align-self: flex-end;
  margin-top: 3vh;
  background-color: #AFD3C7;
  color: #8B7198;
  padding: 1vh 3vw;
  text-align:center;
}

.passwordBound{
  display: flex;
  flex-direction: row;
}

.hideShowPass{
  height: 2.5vh;
  margin-left: 2vw;
  min-height: 20px;
}

.logInForm{
  border:7px solid #8B7198;
  border-radius: 15px;
  margin: 1vh auto;
  padding: 3vh 6vw;

  height: fit-content;
  width: fit-content;

  display: flex;
  flex-direction: column;
  align-items: flex-start;

  font-weight: bold;
}

.logInForm label{
  color:#AFD3C7;
  /* font-weight: bolder; */
  font-size: larger;
}

.logInForm input{
  background-color: #dfdfdf;
  padding-left: 1vw;
  border-radius: 5px;
  border:none;  
  height: 3vh;
  min-height: 25px;
}

.logInForm fieldset{
  border: 0;  
  margin-bottom: 1vh;
  display: flex;
  flex-direction: column;
}

header { 
  line-height: 1.5;
  min-height: fit-content;
  max-height: 25vh;   
  overflow: hidden;
  width: 80vw;
  position:sticky;
  top:0;
  left:0;
  z-index: 2;

  display: flex;
  justify-content: space-between;
  align-items: center;

  border-bottom: 7px solid  var(--color-text);

  /* border:2px solid black; */
  
  background-color: white;
  /* border-radius: 0 0 15px 15px; */

  /* margin-bottom: 1vh; */
}

header img{  
  min-height: 90px;
  max-height: 125px;
  height: 12vh;
  width: 12vh;
  min-width: 90px;
  aspect-ratio: 1/1;  
}

.logo {
  display: block;
  margin: 2vh 0;
}

header div{
  margin-right: 2vw;
}

nav {
  width: fit-content;
  font-size: 2.7vh; 
  font-weight: bolder;
  
  color:  #C6E0D7;
  margin-top: 1rem;
  margin-bottom: 0.5rem;
}

nav a.router-link-exact-active {
  color: var(--color-text); 
}

nav a.router-link-exact-active:hover {
  background-color: transparent;
}

nav a {
  display: inline-block;
  padding: 0 1rem;
  border-left: 1px solid var(--color-border);
}

nav a:first-of-type {
  border: 0;
}

footer{
  background-color: #8B7198;
  position: fixed;
  bottom: 0;
  height: 4vh;
  width: 100%;
  z-index: 2;

  color:var(--color-background-text);
  display:flex;
  justify-content: space-evenly;
}

@media screen and (max-width: 450px) {
  footer{
    font-size: 0.65rem;
    justify-content: space-between;
    align-items: center;
    text-align: center;
  }  
}

#aside_feedback{border:1px solid black;
    position: absolute;
    top:calc(50% - 20vh / 2);
    left: calc(50% - 30vw / 2);
    z-index: 3;    
    width: 30vw;
    min-height: 20vh;
    height: fit-content;

    background-color: var(--color-background-text);
    border-radius: 5px;
    padding:1vh 1vw;

    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    gap: 2vh;
    text-align: center; 
    white-space: pre-line;       
}

.darkness{
    width: 100vw;
    height: 100vh;
    background-color: rgba(0, 0, 0, 0.158);
    position: absolute;
    top:0px;
    left:0px;
    display:inline-block;
    z-index:2;
}

button{
    background-color: #fff;
    color: var(--color-text);
    font-weight: bold;
    border-color: var(--color-text);
    border-radius: 5px;
}
</style>
