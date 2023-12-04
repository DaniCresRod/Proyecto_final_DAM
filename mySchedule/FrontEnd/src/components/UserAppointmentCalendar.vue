<script setup>
// import Calendar from '../services/GraphCalendarServices'



function BuildCalendarArray(){

}

</script>

<template>
    <section>
        <div>
            <p></p>
            <p>Lunes</p>
            <p>Martes</p>
            <p>Miercoles</p>
            <p>Jueves</p>
            <p>Viernes</p>
            <p>Sabado</p>
            <p>Domingo</p>
            <p>hola3</p>
            <p>hola3</p>
            <p>hola3</p>
            <p>hola3</p>
            <p>hola3</p>
            <p>hola3</p>
            <p>hola3</p>           
        </div>
    </section>
</template>

<style scoped>
section{
    max-width: 75vw;

}

div{border:1px solid black;
    padding: 1vh 1vw;
    display: grid;
    grid-template-columns: repeat(8,1fr);
    gap:1vh;
}

p{border:1px solid black;
    width: 8vw;
    text-align: center;
    
}

</style>