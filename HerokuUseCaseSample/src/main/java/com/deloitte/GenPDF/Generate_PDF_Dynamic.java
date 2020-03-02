package com.deloitte.GenPDF;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class Generate_PDF_Dynamic {
    /**
     * @param args the command line arguments
     */
   public static void main(String[] args) {
	//public void myPdf() {
	   
	   try {
		String file_name = "C:\\Temp\\ChillyFlakes.pdf";
		Document document = new Document();
		//Composing PDF 
		try {
		PdfWriter.getInstance(document, new FileOutputStream(file_name));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	   document.open();
	   
	   //Connecting postgres DB
    	 DBConnection app = new  DBConnection();
    	 Connection conn = app.connect();
    	 PreparedStatement ps = null;
    	 ResultSet rs = null;
    	 String query = "SELECT * FROM public.case";
    	 ps = conn.prepareStatement(query);
    	 rs = ps.executeQuery();
         System.out.println("Connected to DB");
        while(rs.next()){
    		 
    		// Paragraph para = new Paragraph(rs.getString("kj")+" "+rs.getString("case_num"));
        	Paragraph para = new Paragraph("Demo PDF Testing" +" "+ rs.getString("case_num"));
        	document.add(para);
    		document.add(new Paragraph());
    		 System.out.println("Created");
    	 }
		Paragraph para1 = new Paragraph("Demo PDF Testing");
		
		document.add(para1);
		document.add(new Paragraph("   "));
		//Add image
		//add table
		PdfPTable table= new PdfPTable(3); 
		PdfPCell c1 = new PdfPCell(new Phrase("Heading 1"));
		table.addCell(c1);
		
		c1 = new PdfPCell(new Phrase("Heading 2"));
		table.addCell(c1);
		
		c1 = new PdfPCell(new Phrase("Heading 3"));
		table.addCell(c1);
		table.setHeaderRows(1);
		
		table.addCell("1,0");
		table.addCell("1,1");
		table.addCell("1,2");
		table.addCell("2,1");
		table.addCell("2,2");
		table.addCell("2,3");
		document.add(table);
    		 document.close();
	   } catch (Exception e)
		{
			System.err.println(e);
		}
    }

}
