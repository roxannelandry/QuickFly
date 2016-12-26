package ca.ulaval.glo4003.quickflyws.infrastructure.paymentSystem.emailService;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import ca.ulaval.glo4003.quickflyws.domain.paymentSystem.TransactionDetails;
import ca.ulaval.glo4003.quickflyws.infrastructure.email.exceptions.UnsentEmailException;
import ca.ulaval.glo4003.quickflyws.service.paymentSystem.TransactionDetailsSender;

public class TransactionDetailsSenderByEmail implements TransactionDetailsSender {

  private static final String ADMIN_EMAIL = "admin@quickFly";
  private static final String HELP_CENTER_PHONE_NUMBER = "1-888-888-8888";

  public void sendTransactionDetails(TransactionDetails transactionDetails) {
    prepareAndSendTransactionEmail(transactionDetails);
  }

  private void prepareAndSendTransactionEmail(TransactionDetails transactionDetails) {
    try {
      Session session = prepareEmailSession();
      MimeMessage message = prepareEmailMessage(transactionDetails, session);
      Transport.send(message);

    } catch (MessagingException mex) {
      throw new UnsentEmailException(
          "A problem happened with the email server. Please call " + HELP_CENTER_PHONE_NUMBER + " to get your transaction informations");
    }
  }

  private Session prepareEmailSession() {
    Properties properties = setEmailProperties();
    Authenticator auth = setEmailServerAuthentificationInformation();

    return Session.getInstance(properties, auth);
  }

  private Properties setEmailProperties() {
    Properties properties = new Properties();
    properties.put("mail.smtp.auth", "true");
    properties.put("mail.smtp.starttls.enable", "true");
    properties.put("mail.smtp.host", "smtp.gmail.com");
    properties.put("mail.smtp.port", "587");

    return properties;
  }

  private Authenticator setEmailServerAuthentificationInformation() {
    Authenticator authenticator = new Authenticator() {
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication("architecture.4003@gmail.com", "mamrARCHITECTURE");
      }
    };

    return authenticator;
  }

  private MimeMessage prepareEmailMessage(TransactionDetails transactionDetails, Session session) throws AddressException, MessagingException {
    MimeMessage message = new MimeMessage(session);
    message.setFrom(new InternetAddress(ADMIN_EMAIL));

    String to = transactionDetails.getCheckoutEmail();
    message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
    message.setSubject("Your purchase confirmation");

    String messageContent = transactionDetails.transactionDetailsToString();
    String ticketsDetails = transactionDetails.ticketsDetailsToString();
    message.setText(messageContent + ticketsDetails);

    return message;
  }

}