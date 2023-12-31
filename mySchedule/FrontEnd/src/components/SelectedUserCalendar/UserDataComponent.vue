<script setup>

const props=defineProps({
    userData: {
        type: Object,
        default: ()=> ({
            name:'',
            alias:'',
            surname1:'',
            surname2:'',
            email:'',
            phone:'',
            notes:'',
            nif:'',
            price: 60,
        }),
    },
})

function showWrongFields(event){
    event.target.style.backgroundColor='transparent';
}

</script>

<template>
    <fieldset id="fieldset_UserData">
        <legend>Datos Personales</legend>
        <section>
            <label for="input_userName">Nombre: </label>
            <input type="text" id="input_userName" v-model="props.userData.name" title="Nombre del usuario" required @focus="showWrongFields">  
        </section>
        <section>
            <label for="input_userAlias">Alias: </label>
            <input type="text" id="input_userAlias" v-model="props.userData.alias" title="Alias del usuario">  
        </section>
        <section>
            <label for="input_userSurName">Apellido: </label>
            <input type="text" id="input_userSurName" v-model="props.userData.surname1" title="Primer apellido del usuario">  
        </section>
        <section>
            <label for="input_userSurName2">Apellido 2º: </label>
            <input type="text" id="input_userSurName2" v-model="props.userData.surname2" title="Segundo apellido del usuario">  
        </section>
        <section>
            <label for="input_userEmail">Email: </label>
            <input type="email" id="input_userEmail" v-model="props.userData.email" title="Email del usuario" required @focus="showWrongFields">  
        </section>
        <section>
            <label for="input_userPhone">Teléfono: </label>
            <input type="text" id="input_userPhone" v-model="props.userData.phone" title="Telefono del usuario" required @focus="showWrongFields">  
        </section>
        <section>
            <label for="input_userNif">Nif: </label>
            <input type="text" id="input_userNif" v-model="props.userData.nif" title="Nif del usuario" required @focus="showWrongFields">  
        </section>
        <section>
            <label for="input_userPrice">Precio por sesión: </label>
            <input type="number" id="input_userPrice" v-model="props.userData.price" title="Precio para el usuario">  
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
    row-gap: 1vh;
}

section{
    max-width: 40vw;
    display: flex;
    flex-direction: column;
    font-size: small;  
    pointer-events: none;  
}

input{
    max-width: 40vw;
    
    border-color: var(--color-border);
    border-style: none;
    border-bottom-style: solid;
}

label{
    font-size: smaller;
}

</style>