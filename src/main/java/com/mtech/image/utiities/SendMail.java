package com.mtech.image.utiities;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Component;

@Component
public class SendMail {
	public void sendEmail(String fileName, String fileUrl, String toUserName, String fromUsername, String toEmail) {
		
		String from = "filelinkshared@gmail.com";
		String password = "zaq1!QAZ";
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");

		Session session = Session.getDefaultInstance(props,
			new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(from,password);
				}
			});

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(toEmail));
			message.setSubject("Shared File");
			message.setText("Hi "+toUserName+",\n\n Please find below the link of shared file which is shared with you by "+fromUsername+".\n This link is valid for 1 minute only." +
					"\n\n"+fileUrl);

			Transport.send(message);

			System.out.println("Email Sent!!");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
}
