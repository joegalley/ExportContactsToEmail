package com.lumere.exportcontactstoemail;

import java.io.UnsupportedEncodingException;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Transport;

import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import android.os.AsyncTask;

public class SendEmail {

	private static final String user = "_EMAIL";
	private static final String pass = "_PASSWORD";

	private Session createSession() {
		Properties p = new Properties();
		p.put("mail.smtp.auth", "true");
		p.put("mail.smtp.starttls.enable", "true");
		p.put("mail.smtp.host", "smtp.gmail.com");
		p.put("mail.smtp.port", "587");

		return Session.getInstance(p, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("THIS.USER", "THIS.PASS");
			}
		});

	}

	private Message createMessage(String to, String subject, String msg_body,
			Session session) throws UnsupportedEncodingException,
			MessagingException {

		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress("_EMAIL", "Frist Last"));
		message.addRecipient(Message.RecipientType.TO, new InternetAddress(to,
				to));
		message.setSubject(subject);
		message.setText(msg_body);
		return message;
	}

	public void send(String to, String subject, String msg_body)
			throws UnsupportedEncodingException, MessagingException {
		Session session = this.createSession();
		session.setDebug(true);
		Message message = this.createMessage(to, subject, msg_body, session);
		new SendEmail.SendEmailTask().execute(message);
	}

	private class SendEmailTask extends AsyncTask<Message, Void, Void> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Message... params) {
			try {
				Transport.send(params[0]);

			} catch (MessagingException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void aVoid) {
			super.onPostExecute(aVoid);

		}
	}
}
