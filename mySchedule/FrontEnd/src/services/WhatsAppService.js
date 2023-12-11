import { myUserStore } from './PiniaServices';
import DateServices from './DateServices';

const myStore = myUserStore();

export function sendWhatsApp(){
    document.getElementById("section_whatsapp").classList.add("invisible");

    let msg=`Hola ${myStore.whatsAppUser.name}, recuerda que hemos cambiado la fecha de la cita que tenías ~(el ${DateServices.changeFormatToDate(myStore.whatsAppUser.oldAppoDate)} a las ${myStore.whatsAppUser.oldAppoStart})~,`
    +` al día *${DateServices.changeFormatToDate(myStore.whatsAppUser.newAppoDate)} a las ${DateServices.removeSeconds(myStore.whatsAppUser.newAppoStart)}*. Un saludo!`;

    //Expresion regular para cambiar los espacios por su representacion %20
    let formattedMsg=msg.replace(/\s/g, "%20");

    let newWindow=window.open(`https://api.whatsapp.com/send?phone=${myStore.whatsAppUser.phone}&text=${formattedMsg}`);
    //newWindow.close();
}