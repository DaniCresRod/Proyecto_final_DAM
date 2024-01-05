<script setup>
import { ref, onBeforeMount } from 'vue';
import dateWorks from '../../services/DateServices';
import ProgramAppoComponent from './ProgramAppoComponent.vue';

const props=defineProps({
    userBasicInfo:Object
})

const appoFinish=ref();
const appoStart=ref();
const dateDDMMYYYY=ref();
const weekDay=ref();

onBeforeMount(() => {
    if(props.userBasicInfo.nextAppoStart!==null){
        appoFinish.value=dateWorks.getEndHour(props.userBasicInfo.nextAppoStart);
        appoStart.value=dateWorks.removeSeconds(props.userBasicInfo.nextAppoStart);
    }
    if(props.userBasicInfo.nextAppoDate!==null){
        dateDDMMYYYY.value=dateWorks.changeFormatToDate(props.userBasicInfo.nextAppoDate)
        weekDay.value=dateWorks.getDayFromDate(props.userBasicInfo.nextAppoDate);
    }
})

</script>

<template>
<div class="userCard">
    <p>
        Nombre: <span>{{ props.userBasicInfo.name }}</span>
    </p>
    <p>
        Alias: <span>{{ props.userBasicInfo.alias }}</span>
    </p>
    <hr>
    <p v-if="props.userBasicInfo.nextAppoDate!==null">
        Próxima cita: <span>{{ weekDay }}, {{ dateDDMMYYYY }}</span>
    </p>
    <p v-else class="NullDateProp">
        <span >Sin cita programada: </span><ProgramAppoComponent :userInfo="props.userBasicInfo"/>
    </p>
    <p v-if="props.userBasicInfo.nextAppoStart!==null">
        de <span>{{appoStart}}</span> a <span>{{ appoFinish }}</span>
    </p>    
</div>

</template>

<style scoped>
.userCard{
    border:1px solid var(--color-border);
    width: 100%;
    /* height: 13vh;     */
    max-height: fit-content;
    /* aspect-ratio: 1.5/1; */
    border-radius: 15px;
    padding: 1vh 3vw ;
    background-color: bisque;
    /* box-shadow: 3px 3px 3px black; */

    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: flex-start;

    font-size: 1.7vh;
}

span{
    font-weight: bold;
}

hr{
    width:90%;
    margin:0.5vh auto;
    border-color:black;
}

.NullDateProp{
    color:red;
    margin: 1vh auto;
    display: flex;
    align-items: baseline;
    flex-wrap: wrap;
    justify-content: center;
}

</style>