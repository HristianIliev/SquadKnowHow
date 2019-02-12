package squadknowhow.restcontrollers;

import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import squadknowhow.response.models.PaymentID;
import squadknowhow.services.PayPalService;

@RestController
@RequestMapping("/api")
public class PayPalController {
  private final PayPalService payPalService;

  @Autowired
  public PayPalController(PayPalService payPalService) {
    this.payPalService = payPalService;
  }

  @RequestMapping(value = "/retrieveMoney", method = RequestMethod.GET)
  @ResponseBody
  public boolean retrieveMoney(@RequestParam("id") int id,
                               @RequestParam("amount") double amount)
          throws PayPalRESTException {
    return this.payPalService.retrieveMoney(id, amount);
  }

  @RequestMapping(value = "/createPayment", method = RequestMethod.POST)
  @ResponseBody
  public PaymentID checkout(@RequestParam("amount") double amount,
                            @RequestParam("isToPromote") boolean isToPromote) {
    System.out.println("in checkout");
    return this.payPalService.createPayment(amount, isToPromote);
  }

  @RequestMapping(value = "/completePayment", method = RequestMethod.POST,
          consumes =
                  {"application/x-www-form-urlencoded;charset=UTF-8"})
  public boolean completePayment(@RequestParam("paymentID") String paymentId,
                                 @RequestParam("payerID") String payerID,
                                 @RequestParam("projectId") String projectId,
                                 @RequestParam("userId") String userId) {
    System.out.println("in completePayment");
    System.out.println(paymentId);
    System.out.println(payerID);
    this.payPalService.completePayment(paymentId,
            payerID,
            Integer.parseInt(projectId),
            Integer.parseInt(userId));
    return true;
  }

  @RequestMapping(value = "/addFunds", method = RequestMethod.POST,
          consumes =
                  {"application/x-www-form-urlencoded;charset=UTF-8"})
  public boolean addFunds(@RequestParam("paymentID") String paymentId,
                          @RequestParam("payerID") String payerID,
                          @RequestParam("userId") String userId) {
    System.out.println("in addFunds");
    System.out.println(paymentId);
    System.out.println(payerID);
    this.payPalService.addFunds(paymentId, payerID, Integer.parseInt(userId));
    return true;
  }

  @RequestMapping(value = "/completePaymentToPromote",
          method = RequestMethod.POST,
          consumes =
                  {"application/x-www-form-urlencoded;charset=UTF-8"})
  public boolean completePaymentToPromote(
          @RequestParam("paymentID") String paymentId,
          @RequestParam("payerID") String payerID,
          @RequestParam("projectId") String projectId,
          @RequestParam("planID") String planId) {
    System.out.println("in completePaymentToPromote");
    System.out.println(paymentId);
    System.out.println(payerID);
    this.payPalService.completePaymentToPromote(paymentId,
            payerID,
            Integer.parseInt(projectId),
            Integer.parseInt(planId));
    return true;
  }
}
