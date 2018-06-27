package squadknowhow.response.models;

public class PaymentID {
  private String paymentID;

  public PaymentID(){

  }

  public PaymentID(String paymentID){
    this.setPaymentID(paymentID);
  }

  public String getPaymentID() {
    return paymentID;
  }

  public void setPaymentID(String paymentID) {
    this.paymentID = paymentID;
  }
}
