import {defineStore} from 'pinia'

export const myUserStore=defineStore('userData', {
    state:()=>({
        user :{
            id:'',
            nif:'',
            name:'',
            surname1:'',
            alias:'',
            email:'',
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
       }
    }),
});
