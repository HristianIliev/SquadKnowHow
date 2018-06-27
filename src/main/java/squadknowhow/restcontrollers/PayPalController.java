package squadknowhow.restcontrollers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import squadknowhow.request.models.PaymentData;
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

  @RequestMapping(value = "/createPayment", method = RequestMethod.POST)
  @ResponseBody
  public PaymentID checkout() {
    System.out.println("in checkout");
    return this.payPalService.createPayment();
  }

  @RequestMapping(value = "/completePayment", method = RequestMethod.POST, consumes = {"application/x-www-form-urlencoded;charset=UTF-8"})
  public boolean completePayment(@RequestParam("paymentID") String paymentId,
                                 @RequestParam("payerID") String payerID,
                                 @RequestParam("projectId") String projectId) {
    System.out.println("in completePayment");
    System.out.println(paymentId);
    System.out.println(payerID);
    this.payPalService.completePayment(paymentId, payerID, Integer.parseInt(projectId));
    return true;
  }
}
