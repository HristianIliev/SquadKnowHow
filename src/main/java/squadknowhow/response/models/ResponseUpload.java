package squadknowhow.response.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseUpload {
  @JsonProperty("error")
  private String errorMsg;

  private boolean success;

  public ResponseUpload(boolean success) {
    this.success = success;
  }

  public ResponseUpload(boolean success, String errorMsg) {
    this.errorMsg = errorMsg;
    this.success = success;
  }

  @Override
  public int hashCode() {
    int result = getErrorMsg() != null ? getErrorMsg().hashCode() : 0;
    result = 31 * result + (isSuccess() ? 1 : 0);
    return result;
  }

  public String getErrorMsg() {
    return errorMsg;
  }

  public void setErrorMsg(String errorMsg) {
    this.errorMsg = errorMsg;
  }

  public boolean isSuccess() {
    return success;
  }

  public void setSuccess(boolean success) {
    this.success = success;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ResponseUpload that = (ResponseUpload) o;

    return isSuccess() == that.isSuccess()
            && (getErrorMsg() != null
            ? getErrorMsg().equals(that.getErrorMsg()) : that.getErrorMsg() == null);
  }
}
