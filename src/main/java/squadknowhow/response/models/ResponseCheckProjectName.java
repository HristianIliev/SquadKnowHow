package squadknowhow.response.models;

public class ResponseCheckProjectName {
  private int status;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ResponseCheckProjectName that = (ResponseCheckProjectName) o;

    return getStatus() == that.getStatus();
  }

  @Override
  public int hashCode() {
    return getStatus();
  }

  public ResponseCheckProjectName(int status) {
    this.setStatus(status);
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }
}
