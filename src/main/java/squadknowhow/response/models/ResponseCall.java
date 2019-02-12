package squadknowhow.response.models;

import java.util.List;
import java.util.Objects;

public class ResponseCall {
  private boolean isSuccessful;
  private List<Integer> ids;
  private List<String> images;
  private List<String> names;

  public ResponseCall(boolean isSuccessful,
                      List<Integer> ids,
                      List<String> images,
                      List<String> names) {
    this.setSuccessful(isSuccessful);
    this.setIds(ids);
    this.setImages(images);
    this.setNames(names);
  }

  public boolean isSuccessful() {
    return isSuccessful;
  }

  public void setSuccessful(boolean successful) {
    isSuccessful = successful;
  }

  public List<Integer> getIds() {
    return ids;
  }

  public void setIds(List<Integer> ids) {
    this.ids = ids;
  }

  public List<String> getImages() {
    return images;
  }

  public void setImages(List<String> images) {
    this.images = images;
  }

  public List<String> getNames() {
    return names;
  }

  public void setNames(List<String> names) {
    this.names = names;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ResponseCall that = (ResponseCall) o;
    return isSuccessful() == that.isSuccessful();
  }

  @Override
  public int hashCode() {
    return Objects.hash(isSuccessful());
  }
}
