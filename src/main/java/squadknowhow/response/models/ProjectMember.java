package squadknowhow.response.models;

public class ProjectMember {
  private int id;
  private String name;
  private String firstName;
  private String lastName;
  private String skillset;
  private String email;

  /**
   * Constructor made for the unit tests.
   *
   * @param id        id
   * @param name      name
   * @param firstName firstName
   * @param lastName  lastName
   * @param skillset  skillset
   * @param email     email
   */
  public ProjectMember(int id,
                       String name,
                       String firstName,
                       String lastName,
                       String skillset,
                       String email) {
    this.setId(id);
    this.setName(name);
    this.setFirstName(firstName);
    this.setLastName(lastName);
    this.setSkillset(skillset);
    this.setEmail(email);
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
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

  public String getSkillset() {
    return skillset;
  }

  public void setSkillset(String skillset) {
    this.skillset = skillset;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}
