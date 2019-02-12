package squadknowhow.response.models;

import java.util.Objects;

public class ResponseProjectId {
  private int id;

  public ResponseProjectId(int id) {
    this.setId(id);
  }

  public int getId() {
    return id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ResponseProjectId that = (ResponseProjectId) o;

    return getId() == that.getId();
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId());
  }

  public void setId(int id) {
    this.id = id;
  }
}
