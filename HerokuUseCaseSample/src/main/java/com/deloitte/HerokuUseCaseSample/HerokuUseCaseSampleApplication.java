package com.deloitte.HerokuUseCaseSample;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.deloitte.GenPDF.DBConnection;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.html.simpleparser.HTMLWorker;
import com.itextpdf.text.pdf.PdfWriter;

@SpringBootApplication
public class HerokuUseCaseSampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(HerokuUseCaseSampleApplication.class, args);
	}

}

// Adkrishna : Added to test Simple Spring boot
@RestController
@RequestMapping("/ContactId") // 1. Creating request map

// class HerokuController{
// @GetMapping("/")
// String hello()
// {
// return "My First Web App";
// }
// }
// Close Simple App
class Generate_PDF_Dynamic { // 2. A new Class for PDF Generation
	@GetMapping(path = "/{contactId}") // 3. Get URL parameter
	public String myPdf(@PathVariable String contactId) { // 4. Get Path variable in URL
		
		Document document = new Document();
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		
		try { // 5. main try-catch block

			String bucketName=System.getenv("BUCKET_NAME");//"gpsdemodoc";
			String bucketKey=System.getenv("BUCKET_KEY");//"AKIAQL377ZCZDGQUCB3H";
			String bucketSecret=System.getenv("BUCKET_SECRET");//"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";
			
			// Composing PDF
			try {
				PdfWriter.getInstance(document, outputStream);
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// PDF creation
			document.open();
			// Connecting postgres DB
			DBConnection app = new DBConnection();
			Connection conn = app.connect();
			PreparedStatement ps = null;
			ResultSet rs = null;
			contactIds = contactId;
			String query = "Select * FROM Salesforce.contact where id = ?",contactIds;
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			System.out.println("Connected to DB");
			// System.out.println(contactId);

			Paragraph para1 = new Paragraph("Demo PDF Testing");

			document.add(para1);
			document.add(new Paragraph("This is a sample document for GPS Demo"));
			document.add(new Paragraph(contactId));
			document.add(new Paragraph("  "));
			while (rs.next()) {

				document.add(new Paragraph(" "));
				Paragraph para = new Paragraph("Contact_Id " + ":" + contactId);
				Paragraph para2 = new Paragraph("Contact Name " + ":" + rs.getString("Name"));
				document.add(para);
				document.add(para2);
				document.add(new Paragraph(" "));
				System.out.println("Created");
				HTMLWorker htmlWorker = new HTMLWorker(document);
				String str = "<html><head></head><body>"
						+ "<a href='https://travel.state.gov/content/travel/en/us-visas/visa-information-resources/forms.html'><b>US Visa Website</b></a>"
						+ "<br/><br/>" + "<h1>US Department of State</h1>"
						+ "<p>The Department of State advises the President and leads the nation in foreign policy issues."
						+ "The State Department negotiates treaties and agreements with foreign entities,"
						+ "and represents the United States at the United Nations."
						+ "<p>You can check your application status online or by phone.  You will need the following information:"
						+ "<P><br><table border='1'><tr><td>Last Name<tr>"
						+ "<td bgcolor='red'>Date of Birth (YYYY-MM-DD)<tr><td>Last Four Digit of SSN</table>"
						+ "</body></html>";

				htmlWorker.parse(new StringReader(str));
			}
			
			document.close();
			
			BasicAWSCredentials creds = new BasicAWSCredentials(bucketKey, bucketSecret); 
			AmazonS3 s3Client = AmazonS3ClientBuilder
											.standard()
											.withRegion(Regions.US_EAST_2)
											.withCredentials(new AWSStaticCredentialsProvider(creds)).build();
			
			ByteArrayInputStream arrayInputStream=new ByteArrayInputStream(outputStream.toByteArray());
			
			final ObjectMetadata metadata = new ObjectMetadata();
			metadata.setSSEAlgorithm(ObjectMetadata.AES_256_SERVER_SIDE_ENCRYPTION);
			metadata.setContentType("application/pdf");
			metadata.setContentLength(arrayInputStream.available());
			final PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, "Document/Demo-"+contactId+".pdf",
			        new BufferedInputStream(arrayInputStream), metadata);

			final PutObjectResult result = s3Client.putObject(putObjectRequest);
			
			System.out.println("Tag: "+result.getETag());
			
			
			
			

		} catch (Exception e) {
			System.err.println(e);
		}
		return "Please check your PDF @temp";
	}

}
