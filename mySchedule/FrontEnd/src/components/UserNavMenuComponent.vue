<script setup>
import { ref, watch } from 'vue';
import { myUserStore } from "../services/PiniaServices.js";
import axiosConnection from '../services/DataServices';

const myStore = myUserStore();

const allUsersBasicInfo = ref([]);
const selectedUser = ref();

async function LoadSelectedUser(event, user){
    if(myStore.user.id!==user){
        selectedUser.value = (await axiosConnection.getUserById(user)).data;
        myStore.user=selectedUser.value;
        myStore.onChanging=false;

        myStore.appo.id=null;
        myStore.appo.appoDate=null;
        myStore.appo.appoStart=null;
        myStore.appo.notes=null;
        myStore.appo.hasBill=false;
        myStore.appo.userID.id=null;
    }    
}

function deleteMyStoreUser(){
    myStore.user ={
            id:'',
            nif:'',
            name:'',
            surname1:'',
            surname2:'',
            alias:'',
            email:'',
            phone:'',
            password:'',
            notes:'',
            price:'',
            appointmentsList:[]
        }
    myStore.onChanging=false;
}

watch(()=> myStore.AllUsers, ()=>{
    allUsersBasicInfo.value=myStore.AllUsers;
    // LoadSelectedUser(myStore.AllUsers[0].id);
});

</script>

<template>
    <section>
        <p v-if="myStore.user.id===''">Listado:</p>
        <p v-else @click="deleteMyStoreUser()"><span>X</span> Ver Todos</p> 
        
        <ul>
            <li v-for="eachUser in allUsersBasicInfo" :key="eachUser" 
                @click="LoadSelectedUser($event, eachUser.id)"
                :class="{selected: eachUser.id === myStore.user.id }">
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
            cursor: pointer;
        } 
        
        li:hover{
            background-color: var(--color-background-text) !important;
            color:var(--color-text1) !important;
        }        
    } 
    p{
        cursor: pointer;
        font-weight: bold;

        span{
            border: 1px solid black;
            border-radius: 25%;
        }
    }
}

@media screen and (max-width: 414px) {
  li{
    font-size: small;
  }  
}

.selected{
    background-color: var(--color-background-text) !important;
    color:var(--color-text1) !important;
}

</style>