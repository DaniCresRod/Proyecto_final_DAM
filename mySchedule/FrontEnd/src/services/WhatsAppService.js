import { myUserStore } from './PiniaServices';

const myStore = myUserStore();

export function sendWhatsApp(msg){
    document.getElementById("div_whatsapp").classList.add("invisible");

    // let msg=`Hola ${myStore.whatsAppUser.name}, recuerda que hemos cambiado la fecha de la cita que tenías (el ${DateServices.changeFormatToDate(myStore.whatsAppUser.oldAppoDate)} a las ${myStore.whatsAppUser.oldAppoStart}),`
    // +` al día *${DateServices.changeFormatToDate(myStore.whatsAppUser.newAppoDate)} a las ${DateServices.removeSeconds(myStore.whatsAppUser.newAppoStart)}*. Un saludo!`;

    //Expresion regular para cambiar los espacios por su representacion %20
    let formattedMsg=msg.replace(/\s/g, "%20");

    let newWindow=window.open(`https://api.whatsapp.com/send?phone=${myStore.whatsAppUser.phone}&text=${formattedMsg}`,'_blank');
    
    setTimeout(()=>{
        newWindow.focus();
        newWindow.close();
    }, 10000)
    //newWindow.close();
}

export function sendReminder(changedDateArray){
    myStore.whatsAppUser.newAppoDate = changedDateArray.appoDate;
    myStore.whatsAppUser.newAppoStart = changedDateArray.appoStart;
    document.getElementById("div_whatsapp").classList.remove("invisible");
    document.querySelector('#div_whatsapp span').textContent=`Avisar del cambio a ${myStore.whatsAppUser.name} por WhatsApp`;           
}