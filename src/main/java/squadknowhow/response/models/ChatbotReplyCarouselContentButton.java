package squadknowhow.response.models;

public class ChatbotReplyCarouselContentButton {
  private String title;
  private String type;
  private String value;

  public ChatbotReplyCarouselContentButton() {

  }

  public ChatbotReplyCarouselContentButton(String title,
                                           String type,
                                           String value) {
    this.setTitle(title);
    this.setType(type);
    this.setValue(value);
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }
}
