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

public class AsyncEmail {

	private String from_address = "EMAIL";
	private String from_password = "PASS";
	private String sender_f_name = "First";
	private String sender_l_name = "Last";

	private String to_address;
	private String subject;
	private String body;

	public AsyncEmail() {

	}

	public void setToAddress(String to_address) {
		this.to_address = to_address;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public void send() throws UnsupportedEncodingException, MessagingException {

		Session session = this.createSession();
		session.setDebug(true);

		Message message = this.createMessage(session);

		new AsyncEmail.SendEmailTask().execute(message);
	}

	private Session createSession() {
		Properties p = new Properties();
		p.put("mail.smtp.auth", "true");
		p.put("mail.smtp.starttls.enable", "true");
		p.put("mail.smtp.host", "smtp.gmail.com");
		p.put("mail.smtp.port", "587");

		return Session.getInstance(p, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(AsyncEmail.this.from_address,
						AsyncEmail.this.from_password);
			}
		});

	}

	private Message createMessage(Session session)
			throws UnsupportedEncodingException, MessagingException {

		Message message = new MimeMessage(session);

		message.setFrom(new InternetAddress(this.from_address));

		message.addRecipient(Message.RecipientType.TO, new InternetAddress(
				this.to_address));
		message.setSubject(this.subject);
		message.setText(this.body);

		return message;
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
