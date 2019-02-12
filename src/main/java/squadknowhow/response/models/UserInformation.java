package squadknowhow.response.models;

public class UserInformation {
  private boolean needsTour;
  private String firstName;

  public UserInformation() {

  }

  public UserInformation(boolean needsTour, String firstName) {
    this.setNeedsTour(needsTour);
    this.setFirstName(firstName);
  }

  public boolean isNeedsTour() {
    return needsTour;
  }

  public void setNeedsTour(boolean needsTour) {
    this.needsTour = needsTour;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    UserInformation that = (UserInformation) o;

    if (isNeedsTour() != that.isNeedsTour()) return false;
    return getFirstName() != null
            ? getFirstName().equals(that.getFirstName())
            : that.getFirstName() == null;
  }

  @Override
  public int hashCode() {
    int result = (isNeedsTour() ? 1 : 0);
    result = 31 * result + (getFirstName() != null
            ? getFirstName().hashCode()
            : 0);
    return result;
  }
}
