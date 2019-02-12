package squadknowhow.request.models;

public class PersonalInfo {
  private String firstName;
  private String lastName;
  private String description;

  public PersonalInfo() {

  }

  public PersonalInfo(String firstName, String lastName, String description) {
    this.setFirstName(firstName);
    this.setLastName(lastName);
    this.setDescription(description);
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
