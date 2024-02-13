<script setup>
import { onBeforeMount } from 'vue'
import router from '../router/index.js';




onBeforeMount(() => {
  const userRol = window.localStorage.getItem("userRol"); 
  if (userRol === "Admin") {
    router.push({ name: 'adminView' }); // Utiliza el nombre de la ruta definido en router.js
  } else if (userRol === "Usuario") {
    router.push({ name: 'userView' }); // Utiliza el nombre de la ruta definido en router.js
  }
  else{
    console.log("Error");
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

@media screen and (max-width: 500px) {
  main{
    width: 100vw;
  }  
}

</style>
