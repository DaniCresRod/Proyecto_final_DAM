<script setup>
import { onBeforeMount } from 'vue'
import router from '../router/index.js';




onBeforeMount(() => {
  const userRol = window.localStorage.getItem("userRol");
  if (userRol === "Admin") {
    router.push({ name: 'adminView' }); // Utiliza el nombre de la ruta definido en router.js
  } else {
    router.push({ name: 'userView' }); // Utiliza el nombre de la ruta definido en router.js
  }

})

</script>

<template> 
  <main>    
    <RouterView/>       
  </main>
  
</template>

<style scoped>

main{
  width: 80vw;
  height: 80vh;  
  overflow-y: hidden;
}

</style>
