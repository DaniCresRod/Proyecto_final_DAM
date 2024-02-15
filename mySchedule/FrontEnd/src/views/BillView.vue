<script setup>
import { myUserStore } from '../services/PiniaServices';
import { onBeforeMount, ref } from 'vue'
import DateServices from '../services/DateServices';
import PDFServices from '../services/PDFServices';

const myUser=ref({});

onBeforeMount(() => {
    myUser.value=myUserStore().user;
})

function downloadBill(id){console.log(id);
    PDFServices.downloadBill(id);
}

</script>

<template>
    <div class="logInForm">
        Tus datos registrados: 
        <hr>       
        <p><span>Nombre:</span> {{ myUser.name }} {{ myUser.surname1 }} {{ myUser.surname2 }}</p>
        <p><span>Nif:</span> {{ myUser.nif }}</p>
        <p><span>Email:</span> {{ myUser.email }}</p>
        <p><span>Tel&eacute;fono:</span> {{ myUser.phone }}</p>        
    </div>

    <div class="logInForm">
        Tus facturas:
        <hr>
        <ul>
            <li v-for="appo of myUser.appointmentsList" :key="appo">
                <p>sesi&oacute;n del {{ DateServices.getDayFromDate(appo.appoDate)  }} {{ DateServices.changeFormatToDate(appo.appoDate) }}</p>
                <p v-if="appo.hasBill===true"><img src="../assets/Images/pdf.png" alt="descargar factura" @click="downloadBill(appo.id)"></p>
                <p v-else></p>
            </li>
        </ul>
        
    </div>

</template>

<style scoped>
div{
    width:50vw;
}
span{
    font-size: 15px;
    color: var(--color-text2);
}
hr{
    width: 100%;
    margin: 1vh auto;
}
p{    
    margin-top:0.3vh;
}
ul{
    list-style: none;
    width: 100%;
    margin: 0;
    padding:0;
}
ul li{
    display: flex;
    justify-content: space-between;
    align-items: center;
    border-bottom: 1px dashed var(--color-text);
    height: 4.5vh;
}
img{
    max-height: 4vh;
}

@media screen and (max-width: 450px) {
  li{
    font-size: smaller;
  }  
  div{
    width:90vw;
  }
}

</style>