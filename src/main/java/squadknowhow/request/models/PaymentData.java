package squadknowhow.request.models;

public class PaymentData {
  private String paymentID;
  private String payerID;

  public PaymentData() {

  }

  public PaymentData(String paymentID, String payerID) {
    this.setPaymentID(paymentID);
    this.setPayerID(payerID);
  }

  public String getPaymentID() {
    return paymentID;
  }

  public void setPaymentID(String paymentID) {
    this.paymentID = paymentID;
  }

  public String getPayerID() {
    return payerID;
  }

  public void setPayerID(String payerID) {
    this.payerID = payerID;
  }
}
