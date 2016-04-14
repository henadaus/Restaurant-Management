/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rescustomerservices;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;
import com.google.api.client.util.store.FileDataStoreFactory;

import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.*;
import com.google.api.services.gmail.Gmail;
import java.io.ByteArrayOutputStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 *
 * @author Ashish
 */
public class GmailQuickstart implements Runnable {

    /**
     * @param args the command line arguments
    /** Application name. */
    
    private static String cusemail;
    private static String cusmessage;
    private static String cussubject;
    private static Message message;
    
    private static MimeMessage a;
    
    public static String getCussubject() {
        return cussubject;
    }

    public static void setCussubject(String cussubject) {
        GmailQuickstart.cussubject = cussubject;
    }

    public static String getCusemail() {
        return cusemail;
    }

    public static void setCusemail(String cusemail) {
        GmailQuickstart.cusemail = cusemail;
    }

    public static String getCusmessage() {
        return cusmessage;
    }

    public static void setCusmessage(String cusmessage) {
        GmailQuickstart.cusmessage = cusmessage;
    }
    
    private static final String APPLICATION_NAME =
        "Ashish";

    /** Directory to store user credentials for this application. */
    private static final java.io.File DATA_STORE_DIR = new java.io.File(
        System.getProperty("user.home"), ".credentials/gmail-java-quickstart2.json");

    /** Global instance of the {@link FileDataStoreFactory}. */
    private static FileDataStoreFactory DATA_STORE_FACTORY;

    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY =
        JacksonFactory.getDefaultInstance();

    /** Global instance of the HTTP transport. */
    private static HttpTransport HTTP_TRANSPORT;

    /** Global instance of the scopes required by this quickstart.
     *
     * If modifying these scopes, delete your previously saved credentials
     * at ~/.credentials/gmail-java-quickstart.json
     */
    private static final List<String> SCOPES =
        Arrays.asList(GmailScopes.GMAIL_COMPOSE,GmailScopes.GMAIL_COMPOSE,GmailScopes.GMAIL_INSERT,GmailScopes.GMAIL_MODIFY,GmailScopes.MAIL_GOOGLE_COM);

    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Creates an authorized Credential object.
     * @return an authorized Credential object.
     * @throws IOException
     */
    public static Credential authorize() throws IOException {
        // Load client secrets.
        InputStream in =
            GmailQuickstart.class.getResourceAsStream("client_secret.json");
        GoogleClientSecrets clientSecrets =
            GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(
                        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(DATA_STORE_FACTORY)
                .setAccessType("offline")
                .build();
        Credential credential = new AuthorizationCodeInstalledApp(
            flow, new LocalServerReceiver()).authorize("user");
        System.out.println(
                "Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
        return credential;
    }

    /**
     * Build and return an authorized Gmail client service.
     * @return an authorized Gmail client service
     * @throws IOException
     */
    public static Gmail getGmailService() throws IOException {
        Credential credential = authorize();
        return new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    
    
    
    public static void sendMessage(Gmail service, String userId, MimeMessage email)
      throws MessagingException, IOException {
    message = createMessageWithEmail(email);
    message = service.users().messages().send(userId, message).execute();

    System.out.println("Message id: " + message.getId());
    System.out.println(message.toPrettyString());
  }

  /**
   * Create a Message from an email
   *
   * @param email Email to be set to raw of message
   * @return Message containing base64url encoded email.
   * @throws IOException
   * @throws MessagingException
   */
  public static Message createMessageWithEmail(MimeMessage email)
      throws MessagingException, IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    email.writeTo(bytes);
    String encodedEmail = Base64.encodeBase64URLSafeString(bytes.toByteArray());
    Message message = new Message();
    message.setRaw(encodedEmail);
    return message;
  }

  /**
   * Create a MimeMessage using the parameters provided.
   *
   * @param to Email address of the receiver.
   * @param from Email address of the sender, the mailbox account.
   * @param subject Subject of the email.
   * @param bodyText Body text of the email.
   * @return MimeMessage to be used to send email.
   * @throws MessagingException
   */
  public static MimeMessage createEmail(String to, String from, String subject,
      String bodyText) throws MessagingException {
    Properties props = new Properties();
    Session session = Session.getDefaultInstance(props, null);

    MimeMessage email = new MimeMessage(session);
    InternetAddress tAddress = new InternetAddress(to);
    InternetAddress fAddress = new InternetAddress(from);

    email.setFrom(new InternetAddress(from));
    email.addRecipient(javax.mail.Message.RecipientType.TO,
                       new InternetAddress(to));
    email.setSubject(subject);
    email.setText(bodyText);
    return email;
  }

  /**
   * Create a MimeMessage using the parameters provided.
   *
   * @param to Email address of the receiver.
   * @param from Email address of the sender, the mailbox account.
   * @param subject Subject of the email.
   * @param bodyText Body text of the email.
   * @param fileDir Path to the directory containing attachment.
   * @param filename Name of file to be attached.
   * @return MimeMessage to be used to send email.
   * @throws MessagingException
   */
  public  MimeMessage createEmailWithAttachment(String to, String from, String subject,
      String bodyText, String fileDir, String filename) throws MessagingException, IOException {
    Properties props = new Properties();
    Session session = Session.getDefaultInstance(props, null);

    MimeMessage email = new MimeMessage(session);
    InternetAddress tAddress = new InternetAddress(to);
    InternetAddress fAddress = new InternetAddress(from);

    email.setFrom(fAddress);
    email.addRecipient(javax.mail.Message.RecipientType.TO, tAddress);
    email.setSubject(subject);

    MimeBodyPart mimeBodyPart = new MimeBodyPart();
    mimeBodyPart.setContent(bodyText, "text/plain");
    mimeBodyPart.setHeader("Content-Type", "text/plain; charset=\"UTF-8\"");

    Multipart multipart = new MimeMultipart();
    multipart.addBodyPart(mimeBodyPart);

    mimeBodyPart = new MimeBodyPart();
    DataSource source = new FileDataSource(fileDir + filename);

    mimeBodyPart.setDataHandler(new DataHandler(source));
    mimeBodyPart.setFileName(filename);
    String contentType = Files.probeContentType(FileSystems.getDefault()
        .getPath(fileDir, filename));
    mimeBodyPart.setHeader("Content-Type", contentType + "; name=\"" + filename + "\"");
    mimeBodyPart.setHeader("Content-Transfer-Encoding", "base64");

    multipart.addBodyPart(mimeBodyPart);

    email.setContent(multipart);
    
    return email;
  }

    @Override
    public void run() {
        
         Gmail service = null;
        try {
            service = getGmailService();
        } catch (IOException ex) {
            Logger.getLogger(GmailQuickstart.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Print the labels in the user's account.
        String user = "me";
        
        
        try {
            a = createEmail(cusemail,"me",cussubject,cusmessage);
        } catch (MessagingException ex) {
            Logger.getLogger(GmailQuickstart.class.getName()).log(Level.SEVERE, null, ex);
        }
      
        try {
            sendMessage(service,"me",a);
        } catch (MessagingException ex) {
            Logger.getLogger(GmailQuickstart.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(GmailQuickstart.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Message Sent");
        
        System.out.println("Email Sent Successfully");
        
    }

    
}
