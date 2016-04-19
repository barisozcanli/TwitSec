package com.peace.twitsec.app.util;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class MailSender {

	private static MailSender instance = new MailSender();

	private static final String SMTP_HOST_NAME = "smtp.gmail.com";
	private static final String SMTP_AUTH_USER = "userhere";
	private static final String SMTP_AUTH_PWD = "passwordhere";

	private MailSender() {
	}

	public static MailSender getInstance() {
		return instance;
	}

	public static void main(String[] args) {
		getInstance().sendMail("barisozcanli@gmail.com", "subject", "messageStr");
	}

	public void sendMail(String to, String subject, String messageStr) {
		Properties props = new Properties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.host", SMTP_HOST_NAME);
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");

		Authenticator auth = new SMTPAuthenticator();
		Session session = Session.getDefaultInstance(props, auth);
		session.setDebug(true);
		// uncomment for debugging infos to stdout
		// mailSession.setDebug(true);

		try {
			Transport transport = session.getTransport();

			Message message = new MimeMessage(session);

			message.setFrom(new InternetAddress(SMTP_AUTH_USER));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
			message.setSubject(subject);
			message.setContent("<h:body>" + messageStr + "</body>", "text/html;     charset=utf-8");

			transport.connect();
			transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
			transport.close();
		} catch (Throwable e) {
			e.printStackTrace();
		}

	}

	private class SMTPAuthenticator extends javax.mail.Authenticator {
		public PasswordAuthentication getPasswordAuthentication() {
			String username = SMTP_AUTH_USER;
			String password = SMTP_AUTH_PWD;
			return new PasswordAuthentication(username, password);
		}
	}
}