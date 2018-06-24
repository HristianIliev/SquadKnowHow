package squadknowhow.request.models;

import javax.validation.constraints.NotNull;

public class TwilioVerificationSendSMSRequest {
  @NotNull
  private String phoneNumber;
  @NotNull
  private String countryCode;
  @NotNull
  private String via;

  public TwilioVerificationSendSMSRequest() {

  }

  public TwilioVerificationSendSMSRequest(String phoneNumber, String countryCode, String via) {
    this.setPhoneNumber(phoneNumber);
    this.setCountryCode(countryCode);
    this.setVia(via);
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public String getCountryCode() {
    return countryCode;
  }

  public void setCountryCode(String countryCode) {
    this.countryCode = countryCode;
  }

  public String getVia() {
    return via;
  }

  public void setVia(String via) {
    this.via = via;
  }
}
