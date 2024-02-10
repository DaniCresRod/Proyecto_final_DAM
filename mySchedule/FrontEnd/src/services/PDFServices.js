import DataServices from "./DataServices";
import { myUserStore } from "./PiniaServices.js"
import { OpenFeedbackDialog } from "./UserFeedbackService.js";

export default{

    async generateBill(){
        let response = await DataServices.generateBill(myUserStore().appo.id);
        console.log(response);
        myUserStore().msgToUser=response.data;
        if(response.data.startsWith("Se generó")){
            myUserStore().appo.hasBill=true;
        }
        OpenFeedbackDialog();
    },

    async downloadBill(){
        let response = await DataServices.downloadBill(myUserStore().appo.id, { responseType: 'arraybuffer' });
        let file = new Blob([response.data], { type: 'application/pdf' });
        let fileURL = URL.createObjectURL(file);
        window.open(fileURL);
    }
}