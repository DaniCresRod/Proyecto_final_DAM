<script setup>
import { myUserStore } from '../../services/PiniaServices';
import DateServices from '../../services/DateServices';
// import { onUpdated } from 'vue'

const myStore=myUserStore();

// onUpdated(() => {
//   document.querySelectorAll("#fieldset_Appos li").forEach((eachAppoContainer, index)=>{
//     let dateText=eachAppoContainer.innerText.split(" ",1)[0].split("/", 3);

//     let myDate=new Date(dateText[2]+"-"+dateText[1]+"-"+dateText[0]);

//     if(myDate >= Date.now()){
//         console.log(index);
//     }

//     // console.log(myDate);
//   })  
// })

</script>

<template>
    <fieldset id="fieldset_Appos">
        <legend>Citas</legend>
        <ul>
            <li v-for="items in myStore.user.appointmentsList" :key="items" @click="$emit('selectedAppoEmit', items.id)">
                {{ DateServices.changeFormatToDate(items.appoDate) }} a las {{ DateServices.removeSeconds(items.appoStart) }}

            </li>
        </ul>
        </fieldset>
</template>

<style scoped lang="scss">

fieldset{
    height: 100%;

    ul{
        list-style: none;
        font-size: small;
        padding-inline-start: 0px;
        
        li{
            text-align: center;
            margin: 0.5vh auto;
            border-bottom: 1px solid var(--color-border);
        }

        li:hover{
            background-color: var(--color-background-text);
            font-weight: bold;
            cursor: pointer;
        }
    }
}
</style>