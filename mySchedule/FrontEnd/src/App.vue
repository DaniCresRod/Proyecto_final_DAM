<script setup>
import { RouterLink, RouterView } from 'vue-router'
import HelloWorld from './components/HelloWorld.vue'
</script>

<template>
  <header>
    <img alt="Vue logo" class="logo" src="@/assets/logo.svg" width="125" height="125" />

    <div class="wrapper">
      <HelloWorld msg="You did it!" />

      <nav>
        <RouterLink to="/">Home</RouterLink>
        <RouterLink to="/about">About</RouterLink>
      </nav>
    </div>
  </header>
  <main>
    <RouterView />
  </main>
  
</template>

<style scoped>
header { border: 2px solid black;
  line-height: 1.5;
  height: 15vh;
  overflow: scroll;
  width: 80vw;
  position:sticky;
  top:0;
  left:0;
  z-index: 2;

  margin-bottom: 1vh;
}

main{
  width: 80vw;
  height: 83vh;
}

.logo {
  display: block;
  margin: 0 auto;
}

nav {
  width: 100%;
  font-size: 12px;  
  margin-top: 2rem;
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
