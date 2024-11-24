package org.example;

import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
import java.io.File;
import java.util.Properties;

public class EmailService {
    private String userEmail;
    private String password;
    private Session session;

    public EmailService(String email, String pass) {
        this.userEmail = email;
        this.password = pass;

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userEmail, password); // app-specific password
            }
        });
    }

    public boolean authenticate() {
        try {
            Transport transport = session.getTransport("smtp");
            transport.connect("smtp.gmail.com", userEmail, password);
            transport.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void sendEmail(String to, String subject, String body, File attachment) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(userEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);

            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(body, "text/html");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);

            if (attachment != null) {
                MimeBodyPart attachmentBodyPart = new MimeBodyPart();
                DataSource source = new FileDataSource(attachment);
                attachmentBodyPart.setDataHandler(new DataHandler(source));
                attachmentBodyPart.setFileName(attachment.getName());
                multipart.addBodyPart(attachmentBodyPart);
            }

            message.setContent(multipart);
            Transport.send(message);
            System.out.println("Email Sent!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Message[] receiveEmails() throws Exception {
        Properties properties = new Properties();
        properties.put("mail.store.protocol", "imaps");

        Session emailSession = Session.getDefaultInstance(properties);
        Store store = emailSession.getStore();
        store.connect("imap.gmail.com", userEmail, password);

        Folder inbox = store.getFolder("INBOX");
        inbox.open(Folder.READ_ONLY);

        return inbox.getMessages();
    }

    public String[] fetchEmails() {
        try {
            Message[] messages = receiveEmails();
            String[] emailAddresses = new String[messages.length];

            for (int i = 0; i < messages.length; i++) {
                emailAddresses[i] = messages[i].getFrom()[0].toString();
            }

            return emailAddresses;
        } catch (Exception e) {
            e.printStackTrace();
            return new String[0];
        }
        
    }
}
