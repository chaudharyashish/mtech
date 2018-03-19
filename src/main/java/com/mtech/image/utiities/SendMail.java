package com.mtech.image.utiities;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class SendMail {
	public void sendEmail(String fileName, String fileUrl, String toUserName, String fromUserName, 
			String toEmail, String linkValidityTimeInSeconds) throws Exception {
		
		try {
			
			String sendersEmailAddress = "filelinkshared@gmail.com";
			
            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.auth", "true");
            props.put("mail.debug", "false");
            props.put("mail.smtp.ssl.enable", "true");
            
            Session session = Session.getInstance(props, new EmailAuth());
            Message msg = new MimeMessage(session);
            
            InternetAddress from = new InternetAddress(sendersEmailAddress);
            msg.setFrom(from);
            
            InternetAddress toAddress = new InternetAddress(toEmail);
            
            msg.setRecipient(Message.RecipientType.TO, toAddress);
            
            msg.setSubject("Test");
            msg.setContent("<html>\n" +
                    "<body>\n" +
                    "\n <h3>Hi " + (StringUtils.isEmpty(toUserName)?"":toUserName) +",</h3> \n" +
                    "A file with name <i>"+ fileName + "</i> is shared by <i>"+ fromUserName+".</i> Please find a link to the file below.\n<br/>"+
                    "<a href=\""+fileUrl+"\">\n" +
                    ""+fileName+"</a>\n\n<br/><br/>" +
                    "<i>Note:This link is valid for "+
                    	((Long.parseLong(linkValidityTimeInSeconds) < 60) 
                    		? linkValidityTimeInSeconds+" seconds"
                    		: Long.parseLong(linkValidityTimeInSeconds)/60+" minutes")
                    +" only.</i>"+
                    "</body>\n" +
                    "</html>", "text/html");
            
            Transport.send(msg);
        } catch (MessagingException ex) {
            ex.printStackTrace();
        }
	}
	
	static class EmailAuth extends Authenticator {

        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
        	String from = "filelinkshared@gmail.com";
        	String password = "zaq1!QAZ";

            return new PasswordAuthentication(from, password);
        }
    }
}
