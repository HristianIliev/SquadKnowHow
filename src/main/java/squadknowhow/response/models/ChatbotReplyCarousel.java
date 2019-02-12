package squadknowhow.response.models;

import java.util.List;

public class ChatbotReplyCarousel {
  private String type;
  private List<ChatbotReplyCarouselContent> content;

  public ChatbotReplyCarousel() {

  }

  public ChatbotReplyCarousel(String type,
                              List<ChatbotReplyCarouselContent> content) {
    this.setType(type);
    this.setContent(content);
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public List<ChatbotReplyCarouselContent> getContent() {
    return content;
  }

  public void setContent(List<ChatbotReplyCarouselContent> content) {
    this.content = content;
  }
}
