package squadknowhow.response.models;

import java.util.Objects;

public class ResponsePagination {
  private int numberOfPages;
  private int numberOfEntries;

  public ResponsePagination(int numberOfPages, int numberOfEntries) {
    this.setNumberOfEntries(numberOfEntries);
    this.setNumberOfPages(numberOfPages);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ResponsePagination that = (ResponsePagination) o;
    return getNumberOfPages() == that.getNumberOfPages()
            && getNumberOfEntries() == that.getNumberOfEntries();
  }

  @Override
  public int hashCode() {

    return Objects.hash(getNumberOfPages(), getNumberOfEntries());
  }

  public int getNumberOfPages() {
    return numberOfPages;
  }

  public void setNumberOfPages(int numberOfPages) {
    this.numberOfPages = numberOfPages;
  }

  public int getNumberOfEntries() {
    return numberOfEntries;
  }

  public void setNumberOfEntries(int numberOfEntries) {
    this.numberOfEntries = numberOfEntries;
  }
}
