import router from '../router';
import { myUserStore } from './PiniaServices';

export function turnToSetUserAppo(userData){
    myUserStore().onChanging=true;
myUserStore().whatsAppUser.appoId='';
    myUserStore().whatsAppUser.userId=userData.id;
    myUserStore().whatsAppUser.name=userData.name;
    myUserStore().whatsAppUser.phone=userData.phone;
    router.push("/AdminView");
}

