package squadknowhow.response.models;

public class ResponseSuccessful {
  private boolean successfull;

  public ResponseSuccessful(boolean successfull) {
    this.setSuccessfull(successfull);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ResponseSuccessful that = (ResponseSuccessful) o;

    return isSuccessfull() == that.isSuccessfull();
  }

  public boolean isSuccessfull() {
    return successfull;
  }

  public void setSuccessfull(boolean successfull) {
    this.successfull = successfull;
  }
}
