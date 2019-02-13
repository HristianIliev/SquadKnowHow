package squadknowhow.services;

import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Currency;
import com.paypal.api.payments.Details;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.api.payments.Payout;
import com.paypal.api.payments.PayoutBatch;
import com.paypal.api.payments.PayoutItem;
import com.paypal.api.payments.PayoutSenderBatchHeader;
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
import squadknowhow.dbmodels.User;
import squadknowhow.response.models.PaymentID;

import java.net.URI;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
  private final IRepository<User> usersRepository;

  @Autowired
  public PayPalService(IRepository<Project> projectsRepository,
                       IRepository<User> usersRepository) {
    this.context = new APIContext(CLIENT_ID, CLIENT_SECRET, "sandbox");

    this.projectsRepository = projectsRepository;
    this.usersRepository = usersRepository;
  }

  public PaymentID createPayment(double amount2, boolean isToPromote) {
    // Set payer details
    Payer payer = new Payer();
    payer.setPaymentMethod("paypal");

    // Set redirect URLs
    RedirectUrls redirectUrls = new RedirectUrls();
    redirectUrls.setCancelUrl("http://localhost:3000/cancel");
    redirectUrls.setReturnUrl("http://localhost:3000/process");

    // Set payment details
    Details details = new Details();
    details.setShipping("0");
    details.setSubtotal(String.format("%.2f", amount2));
    details.setTax("0");

    // Payment amount
    Amount amount = new Amount();
    amount.setCurrency("USD");
    // Total must be equal to sum of shipping, tax and subtotal
    amount.setTotal(String.format("%.2f", amount2));
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
        link.getRel();
      }

      return new PaymentID(createdPayment.getId());
    } catch (PayPalRESTException e) {
      System.err.println(e.getDetails());
      return null;
    }
  }

  public void completePayment(String paymentID, String payerID, int projectID, int userId, String email) {
    Payment payment = new Payment();
    payment.setId(paymentID);

    PaymentExecution paymentExecution = new PaymentExecution();
    paymentExecution.setPayerId(payerID);
    try {
      Payment createdPayment = payment.execute(context, paymentExecution);

      User backer = this.usersRepository.getById(userId);
      if (backer.getId() != this.getUserByEmail(email).getId()) {
        throw new InvalidParameterException("Unauthorised operation done by user with email " + email);
      }

      List<Project> backedProjects = backer.getBackedProjects();

      Project project = this.projectsRepository.getById(projectID);
      List<User> backers = project.getBackers();

      boolean hasBackedBefore = false;
      for (Project backedProject :
              backedProjects) {
        if (backedProject.getId() == project.getId()) {
          hasBackedBefore = true;
        }
      }

      if (!hasBackedBefore) {
        backedProjects.add(project);
        backers.add(backer);
      }

      double paid = Double.parseDouble(createdPayment.getTransactions()
              .get(0).getAmount().getDetails().getSubtotal());
      project.setReceivedMoney(project.getReceivedMoney() + paid);
      project.setTimesBacked(project.getTimesBacked() + 1);

      this.call(project.getTelephone().substring(1));

      this.projectsRepository.update(project);

      User user = this.usersRepository.getById(project.getCreator());
      user.setMoney(user.getMoney() + paid);
      this.usersRepository.update(user);

      System.out.println(createdPayment);
    } catch (PayPalRESTException e) {
      System.err.println(e.getDetails());
    }
  }

  public void completePaymentToPromote(String paymentId, String payerID, int projectID, int planId, String email) {
    Payment payment = new Payment();
    payment.setId(paymentId);

    PaymentExecution paymentExecution = new PaymentExecution();
    paymentExecution.setPayerId(payerID);
    try {
      Project project = this.projectsRepository.getById(projectID);
      if (project.getCreator() != this.getUserByEmail(email).getId()) {
        throw new InvalidParameterException("Unauthorised operation done by user with email " + email);
      }

      Payment createdPayment = payment.execute(context, paymentExecution);

      if (planId == 0) {
        project.setDaysTop(3);
      } else if (planId == 1) {
        project.setDaysTop(7);
      } else {
        project.setDaysTop(30);
        project.setHasReservedPlace(true);
      }

      this.projectsRepository.update(project);

      System.out.println(createdPayment);
    } catch (PayPalRESTException e) {
      System.err.println(e.getDetails());
    }
  }

  private void call(String phoneNumber) {
    Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    String number = "+" + phoneNumber;
    System.out.println("calling in Funding on number " + number);
    if (number.length() != PHONE_NUMBER_LENGTH) {
      return;
    }

    PhoneNumber to = new PhoneNumber(number);
    PhoneNumber from = new PhoneNumber(FROM_NUMBER);
    Call call = Call.creator(to,
            from,
            URI.create(
                    "https://handler.twilio.com/twiml/"
                            + "EH87a0cdd4d586175f4048a761641d5e49"))
            .create();

    System.out.println(call.getSid());
  }

  public boolean retrieveMoney(int id, double amount, String email) throws PayPalRESTException {
    User user = this.usersRepository.getById(id);
    if (user.getId() != this.getUserByEmail(email).getId()) {
      throw new InvalidParameterException("Unauthorised operation done by user with email " + email);
    }

    if (amount <= 1 || amount > user.getMoney()) {
      return false;
    }

    Payout payout = new Payout();

    PayoutSenderBatchHeader senderBatchHeader = new PayoutSenderBatchHeader();
    senderBatchHeader.setEmailSubject("PayPal Email Header");

    Currency amountPayPal = new Currency();
    amountPayPal.setValue(Double.toString(amount)).setCurrency("USD");

    PayoutItem sendTo = new PayoutItem();

    //This can be "Phone" and specify PayPal mobile number on setReceiver
    sendTo.setRecipientType("Email")
            .setReceiver(user.getEmail())
            .setNote("Thanks.")
            .setAmount(amountPayPal);

    List<PayoutItem> items = new ArrayList<>();
    items.add(sendTo);
    //Add more recipients to items list but with same currency as handling
    //different currencies in single batch isn't possible

    payout.setSenderBatchHeader(senderBatchHeader).setItems(items);

    Map<String, String> parameters = new HashMap<>();
    parameters.put("sync_mode", "false");

    PayoutBatch batch = payout.create(this.context, parameters);
    String batchId = batch.getBatchHeader().getPayoutBatchId();

    String jsonResponseStr = Payout.getLastResponse();

    System.out.println(jsonResponseStr);

    user.setMoney(user.getMoney() - amount);
    this.usersRepository.update(user);

    return true;
  }

  public void addFunds(String paymentId, String payerID, int userId, String email) {
    Payment payment = new Payment();
    payment.setId(paymentId);

    PaymentExecution paymentExecution = new PaymentExecution();
    paymentExecution.setPayerId(payerID);
    try {
      Payment createdPayment = payment.execute(context, paymentExecution);

      User user = this.usersRepository.getById(userId);
      if (user.getId() != this.getUserByEmail(email).getId()) {
        throw new InvalidParameterException("Unauthorised operation done by user with email " + email);
      }

      double paid = Double.parseDouble(createdPayment.getTransactions().get(0)
              .getAmount().getDetails().getSubtotal());

      user.setMoney(user.getMoney() + paid);
      this.usersRepository.update(user);

      System.out.println(createdPayment);
    } catch (PayPalRESTException e) {
      System.err.println(e.getDetails());
    }
  }

  private User getUserByEmail(String email) {
    return usersRepository.getAll()
            .stream()
            .filter(u -> u.getEmail().equals(email))
            .findFirst()
            .orElse(null);
  }
}
