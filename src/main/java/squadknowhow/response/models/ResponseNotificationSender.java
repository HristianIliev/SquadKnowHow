package squadknowhow.response.models;

public class ResponseNotificationSender {
  private int id;
  private String name;
  private byte[] picture;

  /**
   * Constructor for the unit tests.
   *
   * @param id      id
   * @param name    name
   * @param picture picture
   */
  public ResponseNotificationSender(int id, String name, byte[] picture) {
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

  public byte[] getPicture() {
    return picture;
  }

  public void setPicture(byte[] picture) {
    this.picture = picture;
  }
}
