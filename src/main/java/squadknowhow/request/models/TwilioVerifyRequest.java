package squadknowhow.request.models;

import javax.validation.constraints.NotNull;

public class TwilioVerifyRequest {
  @NotNull
  private String phoneNumber;
  @NotNull
  private String countryCode;
  @NotNull
  private String token;

  public TwilioVerifyRequest() {
  }

  public TwilioVerifyRequest(String phoneNumber, String countryCode, String token) {
    this.setPhoneNumber(phoneNumber);
    this.setCountryCode(countryCode);
    this.setToken(token);
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

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }
}
