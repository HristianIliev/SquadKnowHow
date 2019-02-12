package squadknowhow.response.models;

import java.util.Objects;

public class ResponseRegister {
  private int id;

  public ResponseRegister() {

  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ResponseRegister that = (ResponseRegister) o;

    return getId() == that.getId();
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId());
  }

  public ResponseRegister(int id) {
    this.setId(id);
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }
}
