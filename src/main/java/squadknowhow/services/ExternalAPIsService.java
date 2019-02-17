package squadknowhow.services;

import com.authy.AuthyApiClient;
import com.authy.api.Params;
import com.authy.api.Verification;
import com.twilio.Twilio;
import com.twilio.exception.ApiException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import squadknowhow.contracts.IRepository;
import squadknowhow.dbmodels.User;

import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class ExternalAPIsService {
  private static final String ACCOUNT_SID = System.getenv("ACCOUNT_SID");
  private static final String AUTH_TOKEN = System.getenv("AUTH_TOKEN");
  private static final String FROM_NUMBER_TELEPHONE = System.getenv("FROM_NUMBER");
  private static final String EMAIL_PASS = System.getenv("EMAIL_PASS");

  private final IRepository<User> usersRepository;
  private final AuthyApiClient authyApiClient;

  @Autowired
  public ExternalAPIsService(AuthyApiClient authyApiClient,
                             IRepository<User> usersRepository) {
    this.authyApiClient = authyApiClient;
    this.usersRepository = usersRepository;
  }

  public void start(String countryCode, String phoneNumber, String via) {
    System.out.println("in start");
    Params params = new Params();
    params.setAttribute("code_length", "4");
    Verification verification = authyApiClient
            .getPhoneVerification()
            .start(phoneNumber, countryCode, via, params);

    if (!verification.isOk()) {
      try {
        throw new Exception("Error requesting phone verification. " + verification.getMessage());
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  public void verify(String countryCode, String phoneNumber, String token) {
    Verification verification = authyApiClient
            .getPhoneVerification()
            .check(phoneNumber, countryCode, token);

    if (!verification.isOk()) {
      try {
        throw new Exception("Error verifying token. " + verification.getMessage());
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  public int sendSMSAndEmail(String telephone,
                             int userId) throws MessagingException {
    int randomNum = ThreadLocalRandom.current().nextInt(1000, 9999 + 1);

    // Sending SMS
    try {
      Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

      Message message = Message
              .creator(new PhoneNumber("+" + telephone),
                      new PhoneNumber("+353861803664"),
                      "Твоят SquadKnowHow"
                              + " верификационен код е: "
                              + randomNum)
              .create();

      System.out.println(message.getSid());
    } catch (ApiException ex) {
      System.out.println(ex.getMessage());
    }

    // Sending email
    Properties props = new Properties();
    props.put("mail.smtp.host", "true");
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.smtp.host", "smtp.gmail.com");
    props.put("mail.smtp.port", "587");
    props.put("mail.smtp.auth", "true");
    Session session = Session.getInstance(props,
            new javax.mail.Authenticator() {
              protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                        "hristian00i.dev@gmail.com",
                        EMAIL_PASS);
              }
            });
    try {
      MimeMessage msg = new MimeMessage(session);
      String to = this.usersRepository.getById(userId).getEmail();
      InternetAddress[] address = InternetAddress.parse(to, true);
      msg.setRecipients(javax.mail.Message.RecipientType.TO, address);
      msg.setSubject("SquadKnowHow верификационен код");
      msg.setSentDate(new Date());
      msg.setText("Твоят SquadKnowHow " + "верификационен код е: " + randomNum);
      msg.setHeader("XPriority", "1");
      Transport.send(msg);
      System.out.println("Mail has been sent successfully");
    } catch (MessagingException mex) {
      System.out.println("Unable to send an email" + mex);
    }

    // Returning the code
    return randomNum;
  }
}
