package squadknowhow.response.models;

import java.util.Objects;

public class ResponseCreateAuction {
  private int responseId;

  public ResponseCreateAuction() {

  }

  public ResponseCreateAuction(int responseId) {
    this.setResponseId(responseId);
  }

  public int getResponseId() {
    return responseId;
  }

  public void setResponseId(int responseId) {
    this.responseId = responseId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ResponseCreateAuction that = (ResponseCreateAuction) o;
    return getResponseId() == that.getResponseId();
  }

  @Override
  public int hashCode() {
    return Objects.hash(getResponseId());
  }
}
