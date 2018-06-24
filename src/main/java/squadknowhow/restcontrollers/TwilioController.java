package squadknowhow.restcontrollers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import squadknowhow.request.models.TwilioVerificationSendSMSRequest;
import squadknowhow.request.models.TwilioVerifyRequest;
import squadknowhow.response.models.Bool;
import squadknowhow.services.TwilioService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class TwilioController {
  private final TwilioService twilioService;

  @Autowired
  public TwilioController(TwilioService twilioService) {
    this.twilioService = twilioService;
  }

  @RequestMapping(value = "/sendSMS", method = RequestMethod.POST)
  public Bool sendSMS(@Valid @RequestBody TwilioVerificationSendSMSRequest requestBody) {
    this.twilioService.start(requestBody.getCountryCode(),
            requestBody.getPhoneNumber(),
            requestBody.getVia());

    return new Bool(true);
  }

  @RequestMapping(value = "/checkVerificationCode", method = RequestMethod.POST)
  public Bool verify(@Valid @RequestBody TwilioVerifyRequest requestBody,
                     HttpSession session) {
    this.twilioService.verify(requestBody.getCountryCode(),
            requestBody.getPhoneNumber(),
            requestBody.getToken());
    session.setAttribute("ph_verified", true);

    return new Bool(true);
  }
}
