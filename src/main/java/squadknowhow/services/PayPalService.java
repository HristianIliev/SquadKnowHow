package squadknowhow.services;

import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Details;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Call;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import squadknowhow.contracts.IRepository;
import squadknowhow.dbmodels.Project;
import squadknowhow.response.models.PaymentID;

import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class PayPalService {
  private static final String CLIENT_ID = System.getenv("PayPal_CLIENT_ID");
  private static final String CLIENT_SECRET = System.getenv("PayPal_CLIENT_SECRET");
  private static final String ACCOUNT_SID = System.getenv("ACCOUNT_SID");
  private static final String AUTH_TOKEN = System.getenv("AUTH_TOKEN");
  private static final String FROM_NUMBER = System.getenv("FROM_NUMBER");
  private static final int PHONE_NUMBER_LENGTH = 13;

  private final APIContext context;
  private final IRepository<Project> projectsRepository;

  @Autowired
  public PayPalService(IRepository<Project> projectsRepository) {
    this.context = new APIContext(CLIENT_ID, CLIENT_SECRET, "sandbox");

    this.projectsRepository = projectsRepository;
  }

  public PaymentID createPayment() {
    // Set payer details
    Payer payer = new Payer();
    payer.setPaymentMethod("paypal");

    // Set redirect URLs
    RedirectUrls redirectUrls = new RedirectUrls();
    redirectUrls.setCancelUrl("http://localhost:3000/cancel");
    redirectUrls.setReturnUrl("http://localhost:3000/process");

    // Set payment details
    Details details = new Details();
    details.setShipping("1");
    details.setSubtotal("5");
    details.setTax("1");

    // Payment amount
    Amount amount = new Amount();
    amount.setCurrency("USD");
    // Total must be equal to sum of shipping, tax and subtotal
    amount.setTotal("7");
    amount.setDetails(details);

    // Transaction information
    Transaction transaction = new Transaction();
    transaction.setAmount(amount);
    transaction.setDescription("This is the payment transaction description.");

    // Add transaction to a list
    List<Transaction> transactions = new ArrayList<>();
    transactions.add(transaction);

    // Add payment details
    Payment payment = new Payment();
    payment.setIntent("sale");
    payment.setPayer(payer);
    payment.setRedirectUrls(redirectUrls);
    payment.setTransactions(transactions);

    // Create payment
    try {
      Payment createdPayment = payment.create(context);

      Iterator links = createdPayment.getLinks().iterator();
      while (links.hasNext()) {
        Links link = (Links) links.next();
        if (link.getRel().equalsIgnoreCase("approval_url")) {
          // REDIRECT USER TO link.getHref()
        }
      }

      return new PaymentID(createdPayment.getId());
    } catch (PayPalRESTException e) {
      System.err.println(e.getDetails());
      return null;
    }
  }

  public void completePayment(String paymentID, String payerID, int projectID) {
    Payment payment = new Payment();
    payment.setId(paymentID);

    PaymentExecution paymentExecution = new PaymentExecution();
    paymentExecution.setPayerId(payerID);
    try {
      Payment createdPayment = payment.execute(context, paymentExecution);

      Project project = this.projectsRepository.getById(projectID);

      double paid = Double.parseDouble(createdPayment.getTransactions().get(0).getAmount().getDetails().getSubtotal());
      project.setReceivedMoney(project.getReceivedMoney() + paid);

      this.call(project.getTelephone());

      this.projectsRepository.update(project);

      System.out.println(createdPayment);
    } catch (PayPalRESTException e) {
      System.err.println(e.getDetails());
    }
  }

  private void call(String phoneNumber) {
    Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    String number = "+359" + phoneNumber;
    if (number.length() != PHONE_NUMBER_LENGTH) {
      return;
    }

    PhoneNumber to = new PhoneNumber(number);
    PhoneNumber from = new PhoneNumber(FROM_NUMBER);
    Call call = Call.creator(to, from, URI.create("https://handler.twilio.com/twiml/EH87a0cdd4d586175f4048a761641d5e49"))
            .create();

    System.out.println(call.getSid());
  }
}
