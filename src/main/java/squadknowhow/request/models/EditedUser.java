package squadknowhow.request.models;

public class EditedUser {
  private int id;
  private String firstName;
  private String lastName;
  private String description;
  private String email;
  private String password;

  public EditedUser() {

  }

  public EditedUser(int id) {
    this.setId(id);
  }

  public EditedUser(int id, String firstName) {
    this.setId(id);
    this.setFirstName(firstName);
  }

  public EditedUser(int id, String firstName, String lastName) {
    this.setId(id);
    this.setFirstName(firstName);
    this.setLastName(lastName);
  }

  public EditedUser(int id,
                    String firstName,
                    String lastName,
                    String description) {
    this.setId(id);
    this.setFirstName(firstName);
    this.setLastName(lastName);
    this.setDescription(description);
  }

  public EditedUser(int id,
                    String firstName,
                    String lastName,
                    String description,
                    String email,
                    String password) {
    this.setId(id);
    this.setFirstName(firstName);
    this.setLastName(lastName);
    this.setDescription(description);
    this.setEmail(email);
    this.setPassword(password);
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

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }
}
