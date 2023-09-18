<script setup>
import UserInfo from '../components/UserInfoComponent.vue'
// import {PiniaStore} from '../services/PiniaServices'
import axiosConnection from '../services/DataServices'
import { ref, onBeforeMount } from 'vue'

const myResponse=ref([]);

async function getInfo(){
    let response = await axiosConnection.getAllUsers();
    myResponse.value=await response.data;
    
    console.log(myResponse.value);

}

onBeforeMount(() => {

    getInfo();    
    
});

</script>

<template>
  <div class="userCards">
    <UserInfo v-for="item in myResponse" :key="item" :user-basic-info="item"/>
  </div>
  
</template>

<style scoped>
.userCards{
  display:flex;
  flex-direction: row;
  justify-content: center;
  column-gap: 2vw;
  row-gap: 2vh;
  padding: 2vh 2vw;
  flex-wrap: wrap;
  
  overflow: auto;
  overflow-x: hidden;

}

</style>
