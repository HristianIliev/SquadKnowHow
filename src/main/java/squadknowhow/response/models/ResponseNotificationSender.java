package squadknowhow.response.models;

public class ResponseNotificationSender {
  private int id;
  private String name;
  private String picture;

  /**
   * Constructor for the unit tests.
   *
   * @param id      id
   * @param name    name
   * @param picture picture
   */
  public ResponseNotificationSender(int id, String name, String picture) {
    this.setId(id);
    this.setName(name);
    this.setPicture(picture);
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

  public String getPicture() {
    return picture;
  }

  public void setPicture(String picture) {
    this.picture = picture;
  }
}
