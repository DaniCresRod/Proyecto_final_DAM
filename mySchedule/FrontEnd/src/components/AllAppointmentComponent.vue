<script setup>
import UserInfo from '../components/UserInfoComponent.vue'
// import {PiniaStore} from '../services/PiniaServices'
import axiosConnection from '../services/DataServices'
import { ref, onBeforeMount, watch} from 'vue'

const props=defineProps({
    myfilter:{
      type:String,
      default:""}, 
    myfilterdate:String,
    myfilterNoAppos:Boolean,
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

  MyFilter(myResponse);

  nullApos.value=myResponse.value.filter(x=>{
    return x.nextAppoDate===null
  }).length;

  emit('nullappocustomers', nullApos.value);   
}

onBeforeMount(() => {  
    getInfo(); 
      
});

watch(()=> props.myfilterdate, ()=>MyFilter(myResponse));
watch(()=> props.myfilterNoAppos, ()=>MyFilter(myResponse));
watch(()=> props.myfilter, ()=>MyFilter(myResponse));

//Filtra según la fecha, el nombre y la visibilidad que hayamos indicado
function MyFilter(theObjectArray){
  myFilteredResponse.value=(theObjectArray.value).filter(x=>{
    if(props.myfilterNoAppos){ 
      if((props.myfilter==="")){
        return (x.nextAppoDate===null);
      }
      else{
        return ((x.nextAppoDate===null) && ((x.name.toLowerCase()).includes(props.myfilter.toLowerCase())||(x.alias.toLowerCase()).includes(props.myfilter.toLowerCase())));
      }      
    }
    else{
      if(props.myfilter!==""){
        return (((x.nextAppoDate)>(props.myfilterdate))&&((x.name.toLowerCase()).includes(props.myfilter.toLowerCase())||(x.alias.toLowerCase()).includes(props.myfilter.toLowerCase())));
      }
      else{
        return ((x.nextAppoDate)>(props.myfilterdate));
      }
    }
  });
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
