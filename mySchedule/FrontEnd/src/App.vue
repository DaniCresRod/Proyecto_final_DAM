<script setup>
import mainView from './views/MainView.vue'
import { ref } from 'vue'

const isLogged=ref(false);
const isAdmin=ref(false);

function toggleImg(){
  if(document.getElementById("myPassword").type=="password"){
    const myImage=document.getElementById("modoPassword");
    myImage.src="/src/assets/Images/iconoMostrar.png";  
    document.getElementById("myPassword").type="text"; 
  }
  else{
    const myImage=document.getElementById("modoPassword");
    myImage.src="/src/assets/Images/iconoOculto.png";  
    document.getElementById("myPassword").type="password";
  }
}

</script>

<template>
    <header>
    <img alt="Brand Logo" class="logo" src="@/assets/Images/brainLogo.png" width="125" height="125" />

    <div class="wrapper">

      <nav v-if="isAdmin && isLogged">
        <RouterLink to="/">Home</RouterLink>
        <RouterLink to="/newAppointment">Nueva Cita</RouterLink>
        <RouterLink to="/allApointment">Próximas citas</RouterLink>
        <RouterLink to="/allApointment">LogOut</RouterLink>
      </nav>      
      <nav v-else-if="isLogged">
        <RouterLink to="/">Mi Cita</RouterLink>
        <RouterLink to="/newAppointment">Mis Facturas</RouterLink>
        <RouterLink to="/allApointment">LogOut</RouterLink>
      </nav>
      <nav v-else>
        
      </nav>

    </div>
  </header>

  <mainView v-if="isLogged"/>
  <form class="logInForm" v-else>
    <fieldset>
      <label for="logInEmail">Email <abbr title="Campo Requerido" aria-label="required">*</abbr></label>
      <input type="text" name="logInEmail" id="myEmail">
    </fieldset>

    <fieldset>
      <label for="logInPassword">Contraseña <abbr title="Campo Requerido" aria-label="required">*</abbr></label>
      <div class="passwordBound">
        <input type="password" name="logInPassword" id="myPassword">
        <img id="modoPassword" src="@/assets/Images/iconoOculto.png" alt="Mostrar u ocultar contraseña" @click="toggleImg()"/>
      </div>      
    </fieldset>

    <input id="btnLogIn" type="button" value="Log In">    
    
  </form>

  <footer>

  </footer>
  
</template>

<style>
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

#modoPassword{
  height: 2.5vh;
  margin-left: 2vw;
  min-height: 20px;
}

.logInForm{
  border:7px solid #8B7198;
  border-radius: 15px;
  margin: 7vh auto;
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
  /* height: 20vh;   */
  overflow: hidden;
  width: 80vw;
  position:sticky;
  top:0;
  left:0;
  z-index: 2;

  display: flex;
  justify-content: space-between;
  align-items: center;

  border-bottom: 7px solid  #8B7198;

  /* border:2px solid black; */
  
  background-color: white;
  /* border-radius: 0 0 15px 15px; */

  margin-bottom: 1vh;
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
}
</style>
