package squadknowhow.response.models;

import java.util.List;

public class ChatbotReplyCarouselContent {
  private String title;
  private String subtitle;
  private String imageUrl;
  private List<ChatbotReplyCarouselContentButton> buttons;

  public ChatbotReplyCarouselContent() {

  }

  public ChatbotReplyCarouselContent(String title,
                                     String subtitle,
                                     String imageUrl,
                                     List<ChatbotReplyCarouselContentButton>
                                             buttons) {
    this.setTitle(title);
    this.setSubtitle(subtitle);
    this.setImageUrl(imageUrl);
    this.setButtons(buttons);
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getSubtitle() {
    return subtitle;
  }

  public void setSubtitle(String subtitle) {
    this.subtitle = subtitle;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public List<ChatbotReplyCarouselContentButton> getButtons() {
    return buttons;
  }

  public void setButtons(List<ChatbotReplyCarouselContentButton> buttons) {
    this.buttons = buttons;
  }
}
