<script setup>
import UserDataComponent from './UserDataComponent.vue';
import { ref, watch } from 'vue'
import { myUserStore } from '../services/PiniaServices';

const myUserData=ref({});
const myStore=myUserStore();

myUserData.value=myStore.user;

watch(()=>myStore.user.name, ()=>{
    myUserData.value=myStore.user;
    console.log(myUserData.value);
})

</script>

<template>
    <section id="section_mainSelectedUserInfo">
        <div id="div_AppoDates">
            <fieldset>
            <legend>Citas</legend>
            <ul>
                <li v-for="(items, index) in myStore.user.appointmentsList" :key="items">
                    {{ items.appoDate }}

                </li>
            </ul>
            </fieldset>
        </div>

        <div id="div_UserData">
            <UserDataComponent :userData="myUserData" />
        </div>

        <div id="div_UserNotes">
            {{ myStore.user.notes }}
        </div>

        <div id="div_AppoNotes">
            {{ myStore.user.notes }}
        </div>

        <div id="div_options">
5
        </div>
    </section>
</template>

<style scoped lang="scss">

#section_mainSelectedUserInfo{
    display:grid;
    grid-template-columns: 1fr 3fr;
    grid-template-rows: 1fr 1fr 1.5fr 0.5fr;
    width: 80vw;
    max-height: 70vh;
    gap: 1vh;
    

    div{border: 1px solid var(--color-border);        
        border-radius: 5px;
    }

    #div_AppoDates{
        grid-row: 1 / span 2;
        align-self: flex-start;
        height: 100%;
        
        fieldset{
            height: 100%;

            ul{
                list-style: none;
            }
        }
        
    }

    #div_AppoNotes{
        grid-column: 1 / span 2;
    }

    #div_options{
        grid-column: 1 / span 2;
    }
}
</style>