<script setup>
import UserInfo from '../components/UserInfoComponent.vue'
// import {PiniaStore} from '../services/PiniaServices'
import axiosConnection from '../services/DataServices'
import { ref, onBeforeMount, watch} from 'vue'

const props=defineProps({
    myfilter:String, 
    myfilterdate:String
})

const emit=defineEmits(['nullappocustomers'])

const myResponse=ref([]);
const myFilteredResponse=ref([]);
const nullApos=ref();

async function getInfo(){
    let response = await axiosConnection.getAllUsers();
    myResponse.value=await response.data;
    myFilteredResponse.value=myResponse.value;
    console.log(myResponse.value);

    //filterByDate(myResponse);

    nullApos.value=myResponse.value.filter(x=>{
      return x.nextAppoDate===null
    }).length.toString();

    console.log(nullApos.value);   
    emit('nullappocustomers', nullApos.value);     

}

onBeforeMount(() => {  
    getInfo(); 
      
});

watch(()=> props.myfilter, ()=>{
  if(props.myfilter===""){
    MyFilter(myResponse);
  }
  else{
    MyFilter(myFilteredResponse);
  }  
})

watch(()=> props.myfilterdate, ()=>filterByDate(myResponse));

//Filtra según la fecha y el nombre que hayamos indicado
function MyFilter(theObjectArray){
  myFilteredResponse.value=(theObjectArray.value).filter(x=>{
    return (((x.nextAppoDate)>(props.myfilterdate))&&((x.name.toLowerCase()).includes(props.myfilter.toLowerCase())||(x.alias.toLowerCase()).includes(props.myfilter.toLowerCase())));
  });

}

//Filtra solo por fecha
function filterByDate(theObjectArray){
  myFilteredResponse.value=(theObjectArray.value).filter(x=>{
    if(x.nextAppoDate!=null){
    return ((x.nextAppoDate)>(props.myfilterdate));
    }})
}

</script>

<template>
  <div class="userCards">
    <UserInfo v-for="item in myFilteredResponse" :key="item" :user-basic-info="item"/>
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
