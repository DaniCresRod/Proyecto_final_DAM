package com.example.mySchedule.services;

import com.example.mySchedule.config.EnvVariablesConfig;
import com.example.mySchedule.models.userModel;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPRow;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URL;
import java.time.LocalDate;

@Service
public class PDFServices {

    @Autowired
    private EnvVariablesConfig variablesConfig;

    Document myDocument;
    FileOutputStream fileOutputStream;

    //Fuente para titulos
    Font titleFont= FontFactory.getFont(FontFactory.HELVETICA, 16);

    //Fuente para Parrafos
    Font paragFont=FontFactory.getFont(FontFactory.HELVETICA, 12);
    Font paragFontBold=FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD);



    public String createFolders(String folderName, LocalDate AppoDate, int billNumber) throws DocumentException{
        //String myPath=System.getProperty("user.home");

        //ClassLoader classLoader = getClass().getClassLoader();
        URL resource = getClass().getResource("/");

        if (resource == null) {
            throw new DocumentException("No se pudo determinar el directorio de la aplicación.");
        }

        String myPath=resource.getFile();
        myPath=myPath.replace("%20", " ");
        String dateFolder = String.valueOf(AppoDate.getMonth())+(AppoDate.getYear());

        File folder = new File(myPath+ "/bills/" + dateFolder + "/" + folderName);
        if (!folder.exists()) {
            boolean success = folder.mkdirs();
            if (!success) {
                throw new DocumentException("No se pudieron crear las carpetas necesarias.");
            }
        }
        return folder.getAbsolutePath()+"\\"+billNumber+".pdf";
    }

    public String createDocument(String folderName, LocalDate AppoDate, int billNumber) throws FileNotFoundException, DocumentException {
        myDocument=new Document(PageSize.A4, 50,50,50,50);

        //ruta para los pdf. Iran guardados en en servidor en carpetas por cada cliente
        LocalDate myDate= LocalDate.now();
        String completePath=createFolders(folderName, AppoDate, billNumber);

        fileOutputStream=new FileOutputStream(completePath);

        //Obtener instancia de PDFWriter
        PdfWriter.getInstance(myDocument, fileOutputStream);

        return completePath;
    }

    public void openPDFDocument(){
        myDocument.open();
    }

    public void addPDFTitle(String myTitle) throws DocumentException, IOException {
        PdfPTable titleTable=new PdfPTable(2);
        PdfPCell titleCell=new PdfPCell(new Phrase(myTitle, titleFont));
//        titleCell.setColspan(5);
        titleCell.setBorderColor(BaseColor.WHITE);
        titleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        PdfPCell logoCell=new PdfPCell();
        logoCell.addElement(addLogo("src/main/resources/static/brainLogo.png"));
        logoCell.setBorderColor(BaseColor.WHITE);

        titleTable.addCell(logoCell);
        titleTable.addCell(titleCell);
        myDocument.add(titleTable);
    }

    public PdfPTable addEnterpriseData() throws DocumentException {
        PdfPTable enterpriseDataTable=new PdfPTable(2);

        PdfPCell nameCell=new PdfPCell(new Phrase(variablesConfig.getE_NAME().toString(), paragFont));
        nameCell.setColspan(2);
        PdfPCell addressCell=new PdfPCell(new Phrase(variablesConfig.getE_ADDRESS().toString(), paragFont));
        addressCell.setColspan(2);
        PdfPCell townCell=new PdfPCell(new Phrase(variablesConfig.getE_TOWN().toString(), paragFont));
        PdfPCell postalCodeCell=new PdfPCell(new Phrase("C.P.: "+variablesConfig.getE_POSTAL().toString(), paragFont));
        PdfPCell phoneCell=new PdfPCell(new Phrase("Teléfono: "+variablesConfig.getE_PHONE().toString(), paragFont));
        phoneCell.setColspan(2);
        PdfPCell emailCell=new PdfPCell(new Phrase(variablesConfig.getE_EMAIL().toString(), paragFont));
        emailCell.setColspan(2);
        PdfPCell cifCell=new PdfPCell(new Phrase("CIF: "+variablesConfig.getE_CIF().toString(), paragFont));
        cifCell.setColspan(2);

        enterpriseDataTable.addCell(nameCell);
        enterpriseDataTable.addCell(addressCell);
        enterpriseDataTable.addCell(townCell);
        enterpriseDataTable.addCell(postalCodeCell);
        enterpriseDataTable.addCell(phoneCell);
        enterpriseDataTable.addCell(emailCell);
        enterpriseDataTable.addCell(cifCell);

        enterpriseDataTable.setWidthPercentage(100);
        enterpriseDataTable.setHorizontalAlignment(Element.ALIGN_LEFT);

        //Quitar bordes de la tabla
        for (PdfPRow row : enterpriseDataTable.getRows()) {
            for (PdfPCell cell : row.getCells()) {
                if(cell!=null){
                    cell.setBorderColor(BaseColor.WHITE);
                }
            }
        }

//        myDocument.add(enterpriseDataTable);
        return enterpriseDataTable;
    }

    public PdfPTable addBillData(int billNumber, String ExpDate) throws DocumentException {
        PdfPTable billDataTable=new PdfPTable(2);

        PdfPCell billNumCell=new PdfPCell(new Phrase("Numero de factura: "+billNumber, paragFont));
        billNumCell.setColspan(2);
        billNumCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        PdfPCell dateCell=new PdfPCell(new Phrase("Fecha de expedición: "+ExpDate, paragFont));
        dateCell.setColspan(2);
        dateCell.setHorizontalAlignment(Element.ALIGN_RIGHT);

        billDataTable.addCell(billNumCell);
        billDataTable.addCell(dateCell);

        billDataTable.setWidthPercentage(80);
        billDataTable.setHorizontalAlignment(Element.ALIGN_RIGHT);

//        myDocument.add(billDataTable);
        return billDataTable;
    }

    public void addBillHeader(int billNumber, String ExpDate) throws DocumentException {
        PdfPTable billHeaderTable=new PdfPTable(2);

        PdfPCell addEnterpriseDataCell=new PdfPCell();
        addEnterpriseDataCell.addElement(this.addEnterpriseData());
        addEnterpriseDataCell.setBorderColor(BaseColor.WHITE);
        PdfPCell billDataTableCell=new PdfPCell();
        billDataTableCell.addElement(this.addBillData(billNumber, ExpDate));
        billDataTableCell.setBorderColor(BaseColor.WHITE);

        billHeaderTable.addCell(addEnterpriseDataCell);
        billHeaderTable.addCell(billDataTableCell);

        billHeaderTable.setWidthPercentage(100);

        myDocument.add(billHeaderTable);
    }

    public void addCustomerData(userModel theUser) throws DocumentException {
        PdfPTable customerTable=new PdfPTable(1);

        PdfPCell headerCell=new PdfPCell(new Phrase("Datos de Cliente: ", paragFontBold));
        PdfPCell nameCell=new PdfPCell(new Phrase("Nombre: "+theUser.getName(), paragFont));
        PdfPCell surNameCell=new PdfPCell(new Phrase("Apellidos: "+theUser.getSurname1()
                +" "+theUser.getSurname2(), paragFont));
        PdfPCell nifCell=new PdfPCell(new Phrase("Con NIF/CIF: "+theUser.getNif(), paragFont));
        PdfPCell phoneCell=new PdfPCell(new Phrase("Telefono: "+theUser.getPhone(), paragFont));
        PdfPCell EmailCell=new PdfPCell(new Phrase("Email: "+theUser.getEmail(), paragFont));


        customerTable.addCell(headerCell);
        customerTable.addCell(nameCell);
        customerTable.addCell(surNameCell);
        customerTable.addCell(nifCell);
        customerTable.addCell(phoneCell);
        customerTable.addCell(EmailCell);

        customerTable.setWidthPercentage(50);
        customerTable.setHorizontalAlignment(Element.ALIGN_RIGHT);

        //Quitar bordes de la tabla
        for (PdfPRow row : customerTable.getRows()) {
            for (PdfPCell cell : row.getCells()) {
                if(cell!=null){
                    cell.setBorderColor(BaseColor.WHITE);
                }
            }
        }

        myDocument.add(customerTable);
    }
    public void addPDFParagraph(String myText) throws DocumentException {
        Paragraph myParagr=new Paragraph();
        myParagr.add(new Phrase(myText, paragFont ));
        myDocument.add(myParagr);
    }

    public void addLineBreak() throws DocumentException {
        Paragraph myParagr=new Paragraph();
        myParagr.add(new Phrase(Chunk.NEWLINE));
        myParagr.add(new Phrase(Chunk.NEWLINE));
        myDocument.add(myParagr);
    }

    public Image addLogo(String imagePath) throws BadElementException, IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("static/brainLogo.png");

        byte[] imageBytes = readBytesFromInputStream(inputStream);
        Image myImage = Image.getInstance(imageBytes);

        myImage.scalePercent(10f);

        return myImage;
    }

    //No se pueden leer directamente los datos de la imagen, hay que transformarla en un array de Bytes
    //Este metodo recoge del Stream los datos de la imagen, y los transforma en un array de Bytes a traves
    //del array data
    //data es como una cuchara, recoge datos del stream con read, y los carga en el stream de salida (buffer)
    //que si es puede interpretar
    private byte[] readBytesFromInputStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        int nRead;
        byte[] data = new byte[1024];

        while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);   //Escribe lo que hay en data en buffer
        }

        buffer.flush();

        return buffer.toByteArray();
    }

    public void closePDFDocument(){
        myDocument.close();
    }
}
