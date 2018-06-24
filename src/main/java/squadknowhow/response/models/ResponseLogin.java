package squadknowhow.response.models;

public class ResponseLogin {
  private String message;
  private int id;

  public ResponseLogin() {

  }

  public ResponseLogin(String message, int id) {
    this.setMessage(message);
    this.setId(id);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ResponseLogin that = (ResponseLogin) o;

    return getId() == that.getId() && getMessage().equals(that.getMessage());
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }
}
