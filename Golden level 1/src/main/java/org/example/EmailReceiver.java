package org.example;

import javax.mail.*;
import javax.mail.search.FlagTerm;
import java.util.Properties;

public class EmailReceiver {

    public static void receiveEmail(String userEmail, String appPassword) {
        // Define the properties needed to connect to the IMAP server
        Properties properties = new Properties();
        properties.put("mail.store.protocol", "imaps");
        properties.put("mail.imap.host", "imap.gmail.com");
        properties.put("mail.imap.port", "993");
        properties.put("mail.imap.ssl.enable", "true"); // SSL is required for IMAP

        try {
            // Create a session with the properties
            Session emailSession = Session.getDefaultInstance(properties);

            // Connect to the IMAP server
            Store emailStore = emailSession.getStore("imaps");
            emailStore.connect("imap.gmail.com", userEmail, appPassword);

            // Access the inbox folder
            Folder inbox = emailStore.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY); // Open the folder in read-only mode

            // Fetch unread messages
            Message[] messages = inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));
            System.out.println("Total Messages: " + messages.length);

            // Iterate over the messages and display the subject and content
            for (Message message : messages) {
                System.out.println("Email Subject: " + message.getSubject());
                System.out.println("From: " + message.getFrom()[0].toString());

                if (message.isMimeType("text/plain")) {
                    System.out.println("Text: " + message.getContent().toString());
                } else if (message.isMimeType("multipart/*")) {
                    Multipart multipart = (Multipart) message.getContent();
                    for (int i = 0; i < multipart.getCount(); i++) {
                        BodyPart bodyPart = multipart.getBodyPart(i);
                        if (bodyPart.isMimeType("text/plain")) {
                            System.out.println("Text: " + bodyPart.getContent().toString());
                        }
                    }
                }
                System.out.println("---------------------------");
            }

            // Close the folder and store
            inbox.close(false);
            emailStore.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
            
    
}
