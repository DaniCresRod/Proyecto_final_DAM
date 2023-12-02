<script setup>
import { ref, watch } from 'vue';
import { myUserStore } from "../services/PiniaServices.js";
import axiosConnection from '../services/DataServices';

const myStore = myUserStore();

const allUsersBasicInfo = ref([]);
const selectedUser = ref();

async function LoadSelectedUser(user){
    if(myStore.user.id!==user){
        selectedUser.value = (await axiosConnection.getUserById(user)).data;
        console.log(selectedUser.value);
        myStore.user=selectedUser.value
    }    
}

watch(()=> myStore.AllUsers, ()=>{
    allUsersBasicInfo.value=myStore.AllUsers;
    console.log(allUsersBasicInfo.value);
    LoadSelectedUser(myStore.AllUsers[0].id);
});

</script>

<template>
    <section>
        Listado: 
        <ul>
            <li v-for="eachUser in allUsersBasicInfo" :key="eachUser" @click="LoadSelectedUser(eachUser.id)">
                {{ eachUser.name }} <br> {{ eachUser.alias }}
            </li>
        </ul>
    </section>


</template>

<style scoped lang="scss">
section{border: 1px solid var(--color-border);
    border-radius: 5px;
    padding: 0.3vh 0.6vw;
    width: 15vw;
    min-width: 65px;
    height: auto;
    max-height: 70vh;
    overflow-y: auto;
    overflow-x: none;

    ul{
        display: flex;
        flex-direction: column;
        align-items:self-start;
        justify-content: left;
        padding: 1vh 1vw;

        li{border: 1px solid var(--color-border);
            list-style: none;
            margin: 0.3vh 0;
            width: 100%; 
            font-weight: bolder;  
            border-radius: 5px;
            padding: 0.3vh 0.6vw;
            word-wrap: break-word;
            text-wrap: pretty;
        } 
        
        li:hover{
            background-color: var(--color-background-text) !important;
            color:var(--color-text1) !important;
        }        
    } 
    ul:hover li {
            color: var(--color-text2);
    }
}

</style>