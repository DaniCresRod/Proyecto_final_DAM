<script setup>
import { myUserStore } from '../../services/PiniaServices';
import { watchEffect } from 'vue'

const myStore=myUserStore();

watchEffect(()=>myStore.onChanging, )

</script>

<template>
    <div>
        <button v-if="!myStore.onChanging">Modificar</button>
        <button v-if="myStore.onChanging">Guardar Cambios</button>
        <button>Nueva cita</button>
    </div>
</template>

<style scoped>
div{
    display: flex;
    flex-direction: row;
    column-gap: 1.5vw;
}

button{
    background-color: #fff;
    color: var(--color-text);
    font-weight: bold;
    border-color: var(--color-text);

}

</style>