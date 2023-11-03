<script setup>
import { ref } from 'vue'
import { RouterLink, RouterView } from 'vue-router'
// import HelloWorld from './components/HelloWorld.vue'

const isAdmin=ref(false);
</script>

<template>
  <header>
    <img alt="Brand Logo" class="logo" src="@/assets/Images/brainLogo.png" width="125" height="125" />

    <div class="wrapper">
      <!-- <HelloWorld msg="Mi clínica" /> -->

      <nav v-if="isAdmin">
        <RouterLink to="/">Home</RouterLink>
        <RouterLink to="/newAppointment">Nueva Cita</RouterLink>
        <RouterLink to="/allApointment">Próximas citas</RouterLink>
      </nav>      
      <nav v-else>
        <RouterLink to="/">Mi Cita</RouterLink>
        <RouterLink to="/newAppointment">Mis Facturas</RouterLink>
        <RouterLink to="/allApointment">LogOut</RouterLink>
      </nav>

    </div>
  </header>
  <main>
    <RouterView />
  </main>

  <footer>

  </footer>
  
</template>

<style scoped>
header { 
  line-height: 1.5;
  height: 15vh;
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
  height: 12vh;
  min-height: 90px;
  min-width: 90px;
  aspect-ratio: 1/1; 
  
}

main{
  width: 80vw;
  height: 80vh;  
}

.logo {
  display: block;
  
  /* margin: 0 auto; */
}

div{
  margin-right: 2vw;
}


nav {
  width: 100%;
  font-size: 2.7vh; 
  font-weight: bolder;
  
  color:  #C6E0D7;
  margin-top: 1rem;
}

nav a.router-link-exact-active {
  color: var(--color-text);
  /* color:red; */
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
