package com.glimworm.common.utils;

/**
 * <p>Title: gwDBtest</p>
 * <p>Description: Your description</p>
 * <p>Copyright: Copyright (c) 1999</p>
 * <p>Company: Glimworm</p>
 * @author J Carter
 * @version
 */
import java.util.*;
import java.io.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
import org.apache.commons.mail.*;


public class SendMail {
	
	public static String TEXTPLAIN = "text/plain";
	public static String TEXTHTML = "text/html";

	public static String notNull(String S, String S1) {
		if (S == null || S.startsWith("null")) return S1; else return S;
	}
	
	static boolean mail_debug=false;
	public static void setDbg(boolean val) {
		mail_debug = val;
	}
	public static void dbg(String S) {
		if (mail_debug) System.err.println("Mail_Debug:"+S);
	}
	public static boolean sendMail(String from, String to, String reply, String subject, String body) {
		return sendMail(from,to,reply,subject,body,TEXTHTML);
	}

	public static boolean sendMail(String from, String to, String reply, String subject, String body, String type) {
		String[] TO = new String[1];
		TO[0] = to;
		return sendMail(from,TO,reply,subject,body,type,"");
	}

	public static boolean sendMail(String from, String to, String reply, String subject, String body, String type, String altText) {
		String[] TO = new String[1];
		TO[0] = to;
		return sendMail(from,TO,reply,subject,body,type,altText);
	}

	public static boolean sendMail(String from, String[] to, String reply, String subject, String body) {
		return sendMail(from,to,reply,subject,body,TEXTHTML,"");
	}

	public static boolean sendMail(String from, String[] to, String reply, String subject, String body, String type) {
		return sendMail(from, to, reply, subject, body, type, "");
	}

	public static InternetAddress _internetAddress(String S) {
		try {
			if (S == null) return new InternetAddress("");
			String[] S1 = S.split("[| ]",2);
			if (S1.length == 1) {
				return new InternetAddress(S1[0]);
			} else {
				return new InternetAddress(S1[0],S1[1]);
			}
		} catch (Exception E) {
//			gwUtils.error("","gwSendMail.java","_internetAddress","converting address["+S+"]",E,true);
			return null;
		}
	}
	public static boolean sendMail(String from, String[] to, String reply, String subject, String body, String type, String altText) {
		return sendMail(from,  to,  reply,  subject,  body,  type,  altText, null);
	}

	public static String Host = "localhost";
	//public static String Host = "62.212.70.30";
	public static boolean sendMail_old(String from, String[] to, String reply, String subject, String body, String type, String altText, String[] attachments) {
		/**
		 * renamed to old 2006-11-29 and replaced by the follwing routine!
		 * JC + JVDL
		 */

		boolean retval = true;

		if (altText == null || altText.trim().length() == 0) {
			altText = "Indien u deze nieuwsbrief/ e-mail niet goed kunt lezen ondersteunt uw e-mail programma geen HTML-mail."+
					  "\n\n" +
					  "Your Email Client does not support MIME encoding. Please upgrade to MIME-enabled Email Client (almost every modern Email Client is MIME-capable). ";
		}


		// moved to static String Host = "localhost";
		if (reply.length() == 0) reply = from;

		try {
			Properties props = System.getProperties();
			dbg("Proerties");
			props.put("mail.smtp.host",Host);
			dbg("put");

			// ***Session sess = Session.getDefaultInstance(props, null); // E678 JC 8-mar-2003
			Session sess = Session.getInstance(props, null);
			dbg("New session");

			// Message

			MimeMessage mess = new MimeMessage(sess);
			dbg("New mime message");
			String[] froms = from.split("[| ]",2);
			mess.setFrom(_internetAddress(from));
			dbg("from");
			InternetAddress[] rep = new InternetAddress[1]; // E677 JC 8 mar
			dbg("New Internetaddress");
			rep[0] = _internetAddress(reply); // E677
			dbg("New new [0] Internetaddress");
			mess.setReplyTo(rep); // E677
			dbg("New reply");

			for (int i =0; i < to.length; i++){
				mess.addRecipient(Message.RecipientType.TO, _internetAddress(to[i]));
			}
			mess.setSubject(subject);
			dbg("Subject");

	/* JC 12-jun-2003 E730 START */
	//		mess.setContent(body, type);

			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setText(altText);
			MimeMultipart multipart = new MimeMultipart();
			multipart.setSubType("alternative");
			multipart.addBodyPart(messageBodyPart);
			dbg("multipart");

			messageBodyPart = new MimeBodyPart();
			messageBodyPart.setContent(body,type);
			multipart.addBodyPart(messageBodyPart);
			dbg("multipart : bodypart");

			/**
			 * this is where to add attachments
			 */
			if (attachments != null) {
				for (int a=0; a < attachments.length; a++) {
					// attach the file to the message
					//BodyPart mbp2a = new MimeBodyPart();
					//mbp2a.setText("attachment "+attachments[a]);
					//multipart.addBodyPart(mbp2a);

					BodyPart mbp2 = new MimeBodyPart();
					FileDataSource fds = new FileDataSource(attachments[a]);
					mbp2.setDataHandler(new DataHandler(fds));
					mbp2.setFileName(fds.getName());
					multipart.addBodyPart(mbp2);
					dbg("multipart : bodypart : attachment : "+attachments[a]);
				}
			}


			mess.setContent(multipart);
			dbg("multipart : set content");

	/* JC 12-jun-2003 E730 END */

			//Send
			// ** Transport transport = sess.getTransport("smtp");
			// ** transport.connect();
			// ** transport.sendMessage(mess, mess.getAllRecipients());
			// ** transport.close();
			Transport.send(mess);
			dbg("multipart : send");
			//gwEvents.insertStatic("","","","");

		} catch (Exception E) {
			dbg(E.getMessage());
			retval = false;
		}

		return retval;
	}
	//****************************************************************************************

	public static boolean sendMail(String from, String[] to, String reply, String subject, String body, String type, String altText, String[] attachments) {

		boolean retval = true;
		String DBG = "";

		if (altText == null || altText.trim().length() == 0) {
			altText = "Indien u deze nieuwsbrief/ e-mail niet goed kunt lezen ondersteunt uw e-mail programma geen HTML-mail."+
					  "\n\n" +
					  "Your Email Client does not support MIME encoding. Please upgrade to MIME-enabled Email Client (almost every modern Email Client is MIME-capable). ";
		}


		// moved to static String Host = "localhost";
		if (reply.length() == 0) reply = from;

		try {
			if(type.equalsIgnoreCase(TEXTPLAIN)){
				//SimpleEmail email = new SimpleEmail();
				MultiPartEmail  email = new MultiPartEmail();
				email.setHostName(Host);
				email.setCharset("UTF-8");
				String[] froms = notNull(from, "").split("[| ]",2);
				if (froms.length == 1) email.setFrom(froms[0]);
				else email.setFrom(froms[0], froms[1]);
				InternetAddress[] rep = new InternetAddress[1]; // E677 JC 8 mar
				rep[0] = _internetAddress(reply); // E677
				String[] reps = notNull(reply, "").split("[| ]",2);
				if (reps.length == 1) email.addReplyTo(reps[0]);
				else email.addReplyTo(reps[0], reps[1]);

				for (int i = 0; i < to.length; i++) {
					String[] tos = notNull(to[i], "").split("[| ]",2);
					if (tos.length == 1) email.addTo(tos[0]);
					else email.addTo(tos[0], tos[1]);
				}
				email.setSubject(subject);
				if (body == null || body.trim().length() == 0) body = " ";
				email.setMsg(body);
				/**
				 * this is where to add attachments
				 */
				if (attachments != null) {
					for (int a = 0; a < attachments.length; a++) {
// add the attachment
						if (attachments[a] != null) {
							
							String[] attachmentsArray = attachments[a].split("[|]");
							
							for (int aa=0; aa < attachmentsArray.length; aa++) {
								EmailAttachment attachment = new EmailAttachment();
								attachment.setPath(attachmentsArray[aa]);
								File afile = new File(attachmentsArray[aa]);
								attachment.setDisposition(EmailAttachment.ATTACHMENT);
								attachment.setDescription(afile.getName());
								attachment.setName(afile.getName());
								email.attach(attachment);
							}
						}
					}
				}

				email.send();

			} else {
				// HTML EMAIL
				HtmlEmail email = new HtmlEmail();
				email.setHostName(Host);
				email.setCharset("UTF-8");

				String[] froms = notNull(from, "").split("[| ]",2);
				if (froms.length == 1) email.setFrom(froms[0]);	else email.setFrom(froms[0], froms[1]);

				InternetAddress[] rep = new InternetAddress[1]; // E677 JC 8 mar
				rep[0] = _internetAddress(reply); // E677
				String[] reps = notNull(reply, "").split("[| ]",2);
				if (reps.length == 1) email.addReplyTo(reps[0]); else email.addReplyTo(reps[0], reps[1]);

				for (int i = 0; i < to.length; i++) {
					String[] tos = notNull(to[i], "").split("[| ]",2);
					if (tos.length == 1) email.addTo(tos[0]);
					else email.addTo(tos[0], tos[1]);
				}
				email.setSubject(subject);

				email.setTextMsg(altText);

				if (body == null || body.trim().length() == 0) body = " ";
				email.setHtmlMsg(body);

				/**
				 * this is where to add attachments
				 */
				if (attachments != null) {
					for (int a = 0; a < attachments.length; a++) {
// add the attachment
						if (attachments[a] != null) {
							DBG += "["+a+"]"+attachments[a]; 
							String[] attachmentsArray = attachments[a].split("[|]");
							
							for (int aa=0; aa < attachmentsArray.length; aa++) {
								EmailAttachment attachment = new EmailAttachment();
								attachment.setPath(attachmentsArray[aa]);
								File afile = new File(attachmentsArray[aa]);
								if (afile.exists() && afile.isFile()) {
									attachment.setDisposition(EmailAttachment.ATTACHMENT);
									attachment.setDescription(afile.getName());
									attachment.setName(afile.getName());
									email.attach(attachment);
								} else {
									com.glimworm.common.logging.logUtils.error("","warning", "email attachment ["+attachmentsArray[aa]+"] does not exist", null);
								}
							}
						}
					}
				}

				email.send();
			}
		} catch (Exception E) {
			dbg(E.getMessage());
			com.glimworm.common.logging.logUtils.error("","", "error sending email, DBG="+DBG, E);
			retval = false;
		}

		return retval;
	}
	//****************************************************************************************


	public static boolean sendMail_tst(String from, String[] to, String reply, String subject, String body, String type, String altText, String[] attachments) {

		boolean retval = true;

		if (altText == null || altText.trim().length() == 0) {
			altText = "Indien u deze nieuwsbrief/ e-mail niet goed kunt lezen ondersteunt uw e-mail programma geen HTML-mail."+
					  "\n\n" +
					  "Your Email Client does not support MIME encoding. Please upgrade to MIME-enabled Email Client (almost every modern Email Client is MIME-capable). ";
		}


		// moved to static String Host = "localhost";
		if (reply.length() == 0) reply = from;

		try {
			Properties props = System.getProperties();
			dbg("Properties");
			props.put("mail.smtp.host",Host);
			dbg("put");

			// ***Session sess = Session.getDefaultInstance(props, null); // E678 JC 8-mar-2003
			Session sess = Session.getInstance(props, null);
			dbg("New session");

			// Message

			MimeMessage mess = new MimeMessage(sess);
			dbg("New mime message");
			String[] froms = from.split("[| ]",2);
			mess.setFrom(_internetAddress(from));
			dbg("from");
			InternetAddress[] rep = new InternetAddress[1]; // E677 JC 8 mar
			dbg("New Internetaddress");
			rep[0] = _internetAddress(reply); // E677
			dbg("New new [0] Internetaddress");
			mess.setReplyTo(rep); // E677
			dbg("New reply");

			for (int i =0; i < to.length; i++){
				mess.addRecipient(Message.RecipientType.TO, _internetAddress(to[i]));
			}

			mess.setSubject(subject);
			dbg("Subject");

	/* JC 12-jun-2003 E730 START */
	//		mess.setContent(body, type);

			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setText(altText);
			MimeMultipart multipart = new MimeMultipart();
			multipart.setSubType("related");
			multipart.addBodyPart(messageBodyPart);
			dbg("multipart");

			messageBodyPart = new MimeBodyPart();
			messageBodyPart.setContent(body,type);
			multipart.addBodyPart(messageBodyPart);
			dbg("multipart : bodypart");

			/**
			 * this is where to add attachments
			 */
			if (attachments != null) {
				for (int a=0; a < attachments.length; a++) {
					// attach the file to the message
					//BodyPart mbp2a = new MimeBodyPart();
					//mbp2a.setText("attachment "+attachments[a]);
					//multipart.addBodyPart(mbp2a);

					//File attach = new File(attachments[a]);
					//InputStream is = new FileInputStream(attach);
					//MimeBodyPart mbp2 = new MimeBodyPart(is);
					MimeBodyPart mbp2 = new MimeBodyPart();
					FileDataSource fds = new FileDataSource(attachments[a]);
					mbp2.setDataHandler(new DataHandler(fds));
					mbp2.setFileName(fds.getName());
					mbp2.setDisposition(mbp2.ATTACHMENT);
					multipart.addBodyPart(mbp2,2);
					dbg("multipart : bodypart : attachment : "+attachments[a]);
					//dbg("multipart : bodypart : attachment : file : exists :"+attach.exists());
					//dbg("multipart : bodypart : attachment : file : canRead :"+attach.canRead());
					//dbg("multipart : bodypart : attachment : file : absoluteFile :"+attach.getAbsoluteFile());
					//dbg("multipart : bodypart : attachment : inputstream :"+is.available());
					//dbg("multipart : bodypart : attachment : file==object :" +attach.equals(mbp2.getContent()));
					dbg("multipart : bodypart : attachment : getFileName :"+mbp2.getFileName());
					dbg("multipart : bodypart : attachment : getContent :"+mbp2.getContent());
					dbg("multipart : bodypart : attachment : size :" +mbp2.getSize());
					dbg("multipart : bodypart : attachment : attachment? :" +mbp2.getDisposition());
					dbg("multipart : bodypart : attachment : contentType :" +mbp2.getContentType());

				}
			}


			mess.setContent(multipart);
			dbg("multipart : set content");

	/* JC 12-jun-2003 E730 END */

			//Send
			// ** Transport transport = sess.getTransport("smtp");
			// ** transport.connect();
			// ** transport.sendMessage(mess, mess.getAllRecipients());
			// ** transport.close();
			Transport.send(mess);
			dbg("multipart : send");
			//gwEvents.insertStatic("","","","");

		} catch (Exception E) {
			E.printStackTrace(System.err);
			dbg(E.getMessage());
			retval = false;
		}

		return retval;
	}

}
