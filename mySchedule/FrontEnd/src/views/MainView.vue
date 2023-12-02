<script setup>
//import { ref } from 'vue'
import { RouterView } from 'vue-router'
import UserNavComponent from '../components/UserNavComponent.vue';

//const isAdmin=ref(false);
</script>

<template> 
  <main>
    <UserNavComponent />
    <!-- <RouterView /> -->
  </main>
  
</template>

<style scoped>

main{border: 1px solid black;
  width: 80vw;
  height: 80vh; 
  
  display: flex;
  flex-direction: row;
  justify-content: space-between;
}

</style>
