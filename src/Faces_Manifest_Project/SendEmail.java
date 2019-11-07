/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Faces_Manifest_Project;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author RUGUTE
 */
public class SendEmail {
   static String host="";
   static String recepient="";
   static String source="";
   static String port="";
   static String user="";
   static String pass="";
   
  private static Properties prop = new Properties();
  private static InputStream input = null;
  private static void loadProperties()
      {
        try {
                input =  new FileInputStream("viral_load-runtime.properties");
                // load a properties file
                prop.load(input);
                
                // get the property value and print it out
		System.out.println(prop.getProperty("mail.host"));
		System.out.println(prop.getProperty("mail.username"));
                System.out.println(prop.getProperty("mail.password"));
		System.out.println(prop.getProperty("mail.port"));
		System.out.println(prop.getProperty("mail.from"));
                System.out.println(prop.getProperty("mail.to"));
                System.out.println(prop.getProperty("mail.cc"));
                
                } 
 catch (IOException ioEx) {
		ioEx.printStackTrace();
	} 
}
   
    public static void Send(String subject,String messagebody) {
                 loadProperties();
        host = prop.getProperty("mail.host");
        user = prop.getProperty("mail.username");
        pass = prop.getProperty("mail.password");
        source = prop.getProperty("mail.from");
        recepient = prop.getProperty("mail.to");
        port =prop.getProperty("mail.port");
      // Recipient's email ID needs to be mentioned.
      String to = recepient;

      // Sender's email ID needs to be mentioned
      String from = source;
      final String username = user;//change accordingly
      final String password = pass;//change accordingly

      // Assuming you are sending email through relay.jangosmtp.net
      //String host = host;

      Properties props = new Properties();
      props.put("mail.smtp.auth", "true");
      props.put("mail.smtp.starttls.enable", "true");
      props.put("mail.smtp.host", host);
      props.put("mail.smtp.port", port);

      // Get the Session object.
      Session session = Session.getInstance(props,
         new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
               return new PasswordAuthentication(username, password);
	   }
         });

      try {
	   // Create a default MimeMessage object.
	   Message message = new MimeMessage(session);
	
	   // Set From: header field of the header.
	   message.setFrom(new InternetAddress(from));
	
	   // Set To: header field of the header.
	   message.setRecipients(Message.RecipientType.TO,
               InternetAddress.parse(to));
	
	   // Set Subject: header field
	   message.setSubject(subject);
	
	   // Now set the actual message
	   message.setText(messagebody);

	   // Send message
	   Transport.send(message);

	   System.out.println("Sent message successfully....");

      } catch (MessagingException e) {
         throw new RuntimeException(e);
      }
   }
    
}
