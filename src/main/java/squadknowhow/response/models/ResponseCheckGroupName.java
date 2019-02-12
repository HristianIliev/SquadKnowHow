package squadknowhow.response.models;

public class ResponseCheckGroupName {
  private int status;

  public ResponseCheckGroupName(int status) {
    this.setStatus(status);
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }
}
