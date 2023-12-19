<script setup>

const props=defineProps({
    userData: Object,
})

</script>

<template>
    <fieldset>
        <legend>Datos Personales</legend>
        <section>
            <label for="input_userName">Nombre: </label>
            <input type="text" id="input_userName" v-model="props.userData.name">  
        </section>
        <section>
            <label for="input_userAlias">Alias: </label>
            <input type="text" id="input_userAlias" v-model="props.userData.alias">  
        </section>
        <section>
            <label for="input_userSurName">Apellido: </label>
            <input type="text" id="input_userSurName" v-model="props.userData.surname1">  
        </section>
        <section>
            <label for="input_userSurName2">Apellido 2º: </label>
            <input type="text" id="input_userSurName2" v-model="props.userData.surname2">  
        </section>
        <section>
            <label for="input_userEmail">Email: </label>
            <input type="text" id="input_userEmail" v-model="props.userData.email">  
        </section>
        <section>
            <label for="input_userPhone">Teléfono: </label>
            <input type="text" id="input_userPhone" v-model="props.userData.phone">  
        </section>
        <section>
            <label for="input_userNif">Nif: </label>
            <input type="text" id="input_userNif" v-model="props.userData.nif">  
        </section>
        <section>
            <label for="input_userPrice">Precio por sesión: </label>
            <input type="text" id="input_userPrice" v-model="props.userData.price">  
        </section>      
    </fieldset>
</template>

<style scoped>

fieldset{
    display:flex;
    flex-direction: row;
    flex-wrap: wrap;
    justify-content: space-between;
    overflow-y: auto;
    max-height: 35vh;
    border-radius: 5px;
    border: 1px solid var(--color-border);
}

section{
    max-width: 40vw;
    display: flex;
    flex-direction: column;
    font-size: small;
    
}

input{
    max-width: 40vw;
    pointer-events: none;
}

</style>