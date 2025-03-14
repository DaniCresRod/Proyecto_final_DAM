package com.example.mySchedule.services;

//-------------------------|0|----------------------
// Esta clase establece los metodos para la interaccion de una llamada http
// con la bas de datos.
//
// @Author: Daniel Crespo Rodriguez
// @Date: sept'23
//-------------------------->o<----------------------



import com.example.mySchedule.jwt.AuthTokenFilter;
import com.example.mySchedule.models.appointmentModel;
import com.example.mySchedule.models.userModel;
import com.example.mySchedule.repositories.RepoAppointment;
import com.example.mySchedule.repositories.RepoUser;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class appoServices {
    @Autowired
    RepoAppointment myRepoAppo;
    @Autowired
    PDFServices myPDFService;
    @Autowired
    DateFormatServices myDateServices;
    @Autowired
    JwtService tokenService;
    @Autowired
    AuthTokenFilter authTokenFilter;
    @Autowired
    RepoUser myUserRepo;


    //Devuelve todas las citas de ese dia
    public List<appointmentModel> readAppoints(LocalDate date){
        return myRepoAppo.findAppoByDate(date);
    }

    //Salvar una cita nueva, si no hay otra ya
    public List<appointmentModel> setAppoint(appointmentModel newAppo){
        //Revisar que no se ha seleccionado una fecha pasada
        if(newAppo.getAppoDate().isBefore(LocalDate.now())){
            return null;
        }
        List<appointmentModel> myList = myRepoAppo.findRepeatedAppoHour(newAppo);
        if(myList.size()==0){
            myRepoAppo.save(newAppo);
            myList.add(newAppo);
            return  myList;
        }
        return myList;
    }

    //Cambiar una cita: devuelve la cita cambiada si esta libre
    //Si no esta libre el horario, devuelve una lista con las citas ya establecidas y que evitan el cambio
    public List<appointmentModel> updateAppoint(appointmentModel newAppo){
        try{
            //Revisar que no se ha seleccionado una fecha pasada
            if(newAppo.getAppoDate().isBefore(LocalDate.now())){
                return null;
            }

            //verificar que existe la cita
            appointmentModel oldAppo=myRepoAppo.findById(newAppo.getId()).get();

            //Verificar que no se está moviendo una cita que ya se celebró
            if(oldAppo.getAppoDate().isBefore(LocalDate.now())){
                return null;
            }

            //Recoger la lista de citas con horario incompatible con la nueva cita
            List<appointmentModel> myList = myRepoAppo.findRepeatedAppoHour(newAppo);

            //Si no hay horarios incompatibles
            if(myList.size()==0){
                //Hacer los cambios
                LocalDate oldDate=oldAppo.getAppoDate();
                LocalTime oldTime=oldAppo.getAppoStart();
                oldAppo.setAppoDate(newAppo.getAppoDate());
                oldAppo.setAppoStart(newAppo.getAppoStart());
                oldAppo.setNotes(oldAppo.getNotes()+
                        "\n"+
                        "Se cambia la cita del "+oldDate+" a las "+oldTime+
                        " al "+newAppo.getAppoDate()+" a las "+newAppo.getAppoStart()+
                        " el día "+myDateServices.formatMyDate(LocalDate.now())+
                        newAppo.getNotes());
                myRepoAppo.save(oldAppo);
                myList.add(oldAppo);
            }
            return myList;
        }
        catch(Exception e){
            return null;
        }
    }

    /**
     * Borra de la bd aquellas citas que sucedan en el futuro.
     * Si ya han sucedido, el borrado será logico.
     * @param id es el id de la cita (AppoModel) que queremos borrar
     * @return Devuelve un mensaje indicando la resolución
     */
    public String deleteAppo(long id) {
        String message;
        try{
            //verificar que existe la tarea
            appointmentModel theAppo=myRepoAppo.findById(id).get();

            if(theAppo.getAppoDate().isAfter(LocalDate.now())){
                myRepoAppo.deleteById(id);

                message="La cita de "+theAppo.getUserID().getAlias()+
                        " para el "+theAppo.getAppoDate()+
                        " a las "+theAppo.getAppoStart()+
                        " se ha borrado definitivamente.";
            }
            else{
                //Borrado Logico:
                theAppo.setDeleted(true);
                theAppo.setNotes(theAppo.getNotes()+
                        "\n"+
                        "Se borra la cita el día "+myDateServices.formatMyDate(LocalDate.now()));
                myRepoAppo.save(theAppo);

                message="La cita de "+theAppo.getUserID().getAlias()+
                        " para el "+theAppo.getAppoDate()+
                        " a las "+theAppo.getAppoStart()+
                        " se ha borrado satisfactoriamente.";
            }

            return message;

        }
        catch(Exception e){
            return "Hubo un error y no se pudo borrar: "+e.getMessage();
        }
    }

    //Permite cambiar las anotaciones de una cita concreta
    public appointmentModel updateAppointDetail(appointmentModel newAppo) {
        try{
            //verificar que existe la cita
            appointmentModel oldAppo=myRepoAppo.findById(newAppo.getId()).get();

            if (oldAppo!=null){
                //Hacer los cambios
                oldAppo.setNotes(oldAppo.getNotes()+
                        "\n"+
                        "Se hacen anotaciones el día "+myDateServices.formatMyDate(LocalDate.now())+":\n"+
                        newAppo.getNotes());
                myRepoAppo.save(oldAppo);
            }
            return oldAppo;

        }
        catch(Exception e){
            return null;
        }

    }

    @Transactional
    public String generateBill(long id){
        appointmentModel theAppo=null;
        int billNumber=-1;
        String feedbackMsg=null;
        String completePath=null;
        try{
            theAppo=myRepoAppo.findById(id).get();
            billNumber= myRepoAppo.findBillNumber()+1;
            long userId=theAppo.getUserID().getId();
            String folderName=userId+"_"
                    +theAppo.getUserID().getName().replaceAll("\\s+", "").trim()
                    +(theAppo.getUserID().getSurname1()!=null ? theAppo.getUserID().getSurname1().replaceAll("\\s+", "").trim(): "");

            completePath=myPDFService.createDocument(folderName, theAppo.getAppoDate(), billNumber);
            myPDFService.openPDFDocument();

            String billTitle="Factura";
            myPDFService.addPDFTitle(billTitle);

            myPDFService.addLineBreak();
            myPDFService.addBillHeader(billNumber, myDateServices.formatMyDate(LocalDate.now()));

            myPDFService.addLineBreak();
            myPDFService.addCustomerData(theAppo.getUserID());

            myPDFService.addLineBreak();
            String billBody="Se expende esta factura de "+theAppo.getUserID().getPrice()+"€"+
                    " por los servicios de terapia del día "+myDateServices.formatMyDate(theAppo.getAppoDate());
            myPDFService.addPDFParagraph(billBody);

            myPDFService.addLineBreak();
            String billConcept="Servicios terapeuticos";
            String expeditionDate=myDateServices.formatMyDate(theAppo.getAppoDate());
            myPDFService.addBillDetailTable(theAppo.getUserID().getPrice(), billConcept, expeditionDate);

            myPDFService.addLineBreak();
            myPDFService.addPDFParagraph("Sello y firma: ");

            myPDFService.closePDFDocument();

            feedbackMsg="Se generó la factura para "+theAppo.getUserID().getName()
                    +" "
                    +(theAppo.getUserID().getSurname1()!=null ? theAppo.getUserID().getSurname1():"")
                    +" "
                    +(theAppo.getUserID().getSurname2()!=null ? theAppo.getUserID().getSurname2():"")
                    +" con nif: "+theAppo.getUserID().getNif()
                    +", con número "+billNumber;

            theAppo.setNotes(theAppo.getNotes()+
                    "\n"+
                    feedbackMsg+" el día "+myDateServices.formatMyDate(LocalDate.now())+":\n");
        }
        catch(Exception e){
            completePath=null;
            feedbackMsg= "Se produjo un error y no se puede generar la factura";
        }
        finally {
            theAppo.setBillPath(completePath);
            myRepoAppo.save(theAppo);
            return feedbackMsg;
        }
    }

    public byte[] getPDFDocument(long appoId, HttpServletRequest request){

        try{
            appointmentModel myAppo=myRepoAppo.findById(appoId).get();
            final String token = authTokenFilter.getTokenFromRequest(request);
            final String requesterNIF = tokenService.getUsernameFromToken(token);   //Devuelve el NIF de la persona que lo solicita

            userModel requesterUser=myUserRepo.findByUserNif(requesterNIF).orElse(null);

            if(requesterUser.getRol()!= userModel.UserType.Admin){
                String nif1=myAppo.getUserID().getNif();
                String nif2=requesterNIF;
                if(requesterNIF.equals(myAppo.getUserID().getNif())){
                    byte[] pdfByteArray=myPDFService.getPDFbytes(myAppo.getBillPath());
                    //myPDFService.openPDF(pdfByteArray);
                    return pdfByteArray;
                }
                else{
                    return null;
                }
            }
            else if(requesterUser.getRol()== userModel.UserType.Admin){
                byte[] pdfByteArray=myPDFService.getPDFbytes(myAppo.getBillPath());

                //myPDFService.openPDF(pdfByteArray);
                return pdfByteArray;
            }
            else{
                return null;
            }
        }
        catch(Exception e){
            e.getStackTrace();
            return null;
        }
    }
}
