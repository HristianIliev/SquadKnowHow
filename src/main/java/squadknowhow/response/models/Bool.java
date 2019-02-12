package squadknowhow.response.models;

public class Bool {
  private boolean ok;

  public Bool() {

  }

  public Bool(boolean ok) {
    this.setOk(ok);
  }

  public boolean isOk() {
    return ok;
  }

  public void setOk(boolean ok) {
    this.ok = ok;
  }
}
