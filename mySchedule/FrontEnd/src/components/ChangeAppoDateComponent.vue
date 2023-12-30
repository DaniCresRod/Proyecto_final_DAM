<script setup>
import { myUserStore } from '../services/PiniaServices';
import DataServices from '../services/DataServices';

const props=defineProps({
    myOldDate:{
        type:Object,
        default:()=>({
            appoDate:'',
            appoStart:'',
            id:'',
            notes:'',
            userID:{
                id:'',
            }
        })
    }
})


</script>

<template>
    <div>
        
    </div>
</template>

<style scoped>

</style>