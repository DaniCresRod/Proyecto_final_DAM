package com.example.mySchedule.services;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URL;
import java.time.LocalDate;

@Service
public class PDFServices {

    Document myDocument;
    FileOutputStream fileOutputStream;

    //Fuente para titulos
    Font titleFont= FontFactory.getFont(FontFactory.COURIER, 16);

    //Fuente para Parrafos
    Font paragFont=FontFactory.getFont(FontFactory.COURIER, 12);

    public String createFolders(long AppoId, LocalDate AppoDate, int billNumber) throws DocumentException{
        //String myPath=System.getProperty("user.home");

        //ClassLoader classLoader = getClass().getClassLoader();
        URL resource = getClass().getResource("/");

        if (resource == null) {
            throw new DocumentException("No se pudo determinar el directorio de la aplicación.");
        }

        String myPath=resource.getFile();

        String dateFolder = String.valueOf(AppoDate.getMonth())+(AppoDate.getYear());

        File folder = new File(myPath+ "/Desktop/" + dateFolder + "/" + AppoId);
        if (!folder.exists()) {
            boolean success = folder.mkdirs();
            if (!success) {
                throw new DocumentException("No se pudieron crear las carpetas necesarias.");
            }
        }
        return folder.getAbsolutePath()+"\\"+billNumber+".pdf";
    }

    public String createDocument(long AppoId, LocalDate AppoDate, int billNumber) throws FileNotFoundException, DocumentException {
        myDocument=new Document(PageSize.A4, 35,30,50,50);

        //ruta para los pdf. Iran guardados en en servidor en carpetas por cada cliente
        LocalDate myDate= LocalDate.now();
        String completePath=createFolders(AppoId, AppoDate, billNumber);

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
