package squadknowhow.response.models;

public class ChatbotReplyText {
  private String type;
  private String content;

  public ChatbotReplyText() {

  }

  public ChatbotReplyText(String type, String content) {
    this.setType(type);
    this.setContent(content);
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }
}
