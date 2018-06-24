package squadknowhow.services;

import com.authy.AuthyApiClient;
import com.authy.api.Params;
import com.authy.api.Verification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TwilioService {
  private AuthyApiClient authyApiClient;

  @Autowired
  public TwilioService(AuthyApiClient authyApiClient) {
    this.authyApiClient = authyApiClient;
  }

  public void start(String countryCode, String phoneNumber, String via) {
    Params params = new Params();
    params.setAttribute("code_length", "4");
    Verification verification = authyApiClient
            .getPhoneVerification()
            .start(phoneNumber, countryCode, via, params);

    if (!verification.isOk()) {
      try {
        throw new Exception("Error requesting phone verification. " +
                verification.getMessage());
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
}
