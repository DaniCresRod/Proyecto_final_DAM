package com.example.mySchedule.services;

import com.example.mySchedule.config.EnvVariablesConfig;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPRow;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
        myDocument=new Document(PageSize.A4, 35,30,50,50);

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

    public void addPDFTitle(String myTitle) throws DocumentException {
        PdfPTable titleTable=new PdfPTable(1);
        PdfPCell titleCell=new PdfPCell(new Phrase(myTitle, titleFont));
        titleCell.setColspan(5);
        titleCell.setBorderColor(BaseColor.WHITE);
        titleCell.setHorizontalAlignment(Element.ALIGN_CENTER);

        titleTable.addCell(titleCell);
        myDocument.add(titleTable);
    }

    public void addEnterpriseData() throws DocumentException {
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

        enterpriseDataTable.setWidthPercentage(40);
        enterpriseDataTable.setHorizontalAlignment(Element.ALIGN_LEFT);

        //Quitar bordes de la tabla
        for (PdfPRow row : enterpriseDataTable.getRows()) {
            for (PdfPCell cell : row.getCells()) {
                if(cell!=null){
                    cell.setBorderColor(BaseColor.WHITE);
                }
            }
        }

        myDocument.add(enterpriseDataTable);
    }

    public void addBillData(int billNumber, String ExpDate) throws DocumentException {
        PdfPTable billDataTable=new PdfPTable(2);

        PdfPCell billNumCell=new PdfPCell(new Phrase("Numero de factura: "+billNumber, paragFont));
        billNumCell.setColspan(2);
        PdfPCell dateCell=new PdfPCell(new Phrase("Fecha de expedición: "+ExpDate, paragFont));
        dateCell.setColspan(2);

        billDataTable.addCell(billNumCell);
        billDataTable.addCell(dateCell);

        billDataTable.setWidthPercentage(40);
        billDataTable.setHorizontalAlignment(Element.ALIGN_RIGHT);

        myDocument.add(billDataTable);
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

    public void closePDFDocument(){
        myDocument.close();
    }
}
