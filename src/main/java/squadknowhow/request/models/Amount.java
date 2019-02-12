package squadknowhow.request.models;

public class Amount {
  private double amount;

  public Amount() {

  }

  public Amount(double amount) {
    this.setAmount(amount);
  }

  public double getAmount() {
    return amount;
  }

  public void setAmount(double amount) {
    this.amount = amount;
  }
}
