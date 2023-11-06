<script setup>
import { ref } from 'vue'
import { RouterLink, RouterView } from 'vue-router'
// import HelloWorld from './components/HelloWorld.vue'

const isAdmin=ref(false);
</script>

<template> 
  <main>
    <RouterView />
  </main>
  
</template>

<style scoped>

main{
  width: 80vw;
  height: 80vh;  
}

</style>
