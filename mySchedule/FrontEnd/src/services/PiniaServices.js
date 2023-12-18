import {defineStore} from 'pinia'

export const myUserStore=defineStore('userData', {
    state:()=>({
        user :{
            id:'',
            nif:'',
            name:'',
            surname1:'',
            surname2:'',
            alias:'',
            email:'',
            phone:'',
            password:'',
            notes:'',
            price:'',
            appointmentsList:[]
        },
       appo:{
            id:'',
            appoDate:'',
            appoStart:'',
            notes:'',
            userID:{}
       },
       AllUsers:[],
       whatsAppUser:{
            name:'',
            surname1:'',
            oldAppoDate:'',
            oldAppoStart:'',
            newAppoDate:'',
            newAppoStart:'',
            phone:'',
            userId:'',
            appoId:'',  
            indexOfArray:'',      
       },
       onChanging: false,
    }),
});

