<script setup>
import { RouterLink, RouterView } from 'vue-router'
import HelloWorld from './components/HelloWorld.vue'
</script>

<template>
  <header>
    <img alt="Brand Logo" class="logo" src="@/assets/Images/brainLogo.png" width="125" height="125" />

    <div class="wrapper">
      <HelloWorld msg="Mi clínica" />

      <nav>
        <RouterLink to="/">Home</RouterLink>
        <RouterLink to="/newAppointment">Nueva Cita</RouterLink>
        <RouterLink to="/allApointment">Próximas citas</RouterLink>
      </nav>
    </div>
  </header>
  <main>
    <RouterView />
  </main>
  
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

  border:2px solid black;
  
  background-color: #FEE2C7;
  border-radius: 0 0 15px 15px;

  margin-bottom: 1vh;
}

header img{
  height: 12vh;
  aspect-ratio: 1 / 1;
  
}

main{
  width: 80vw;
  height: 83vh;
  
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
  font-size: 12px;  
  margin-top: 1rem;
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
</style>
