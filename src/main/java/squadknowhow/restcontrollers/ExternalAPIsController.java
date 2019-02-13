package squadknowhow.restcontrollers;

import com.nexmo.client.NexmoClient;
import com.nexmo.client.NexmoClientException;
import com.nexmo.client.auth.AuthMethod;
import com.nexmo.client.auth.TokenAuthMethod;
import com.nexmo.client.verify.VerifyResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import squadknowhow.services.ExternalAPIsService;

import javax.mail.MessagingException;
import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ExternalAPIsController {
  private final ExternalAPIsService externalAPIsService;

  @Autowired
  public ExternalAPIsController(ExternalAPIsService externalAPIsService) {
    this.externalAPIsService = externalAPIsService;
  }

  private VerifyResult request = null;


  @RequestMapping(value = "/sendVerificationSMS", method = RequestMethod.GET)
  @ResponseBody
  public int sendVerificationSMSAndEmail(@RequestParam("telephone") String telephone,
                                         @RequestParam("userId") int userId) throws MessagingException {
    return this.externalAPIsService.sendSMSAndEmail(telephone, userId);
  }

  @RequestMapping(value = "/checkVerificationCode", method = RequestMethod.GET)
  public boolean checkVerificationCode(@RequestParam("code") String code) throws IOException, NexmoClientException {
    AuthMethod auth = new TokenAuthMethod("33048940", "LQBuODK2rXh2LWzW");
    NexmoClient client = new NexmoClient(auth);

    client.getVerifyClient().check(request.getRequestId(), code);

    return true;
  }
}
